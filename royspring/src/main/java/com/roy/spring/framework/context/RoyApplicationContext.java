package com.roy.spring.framework.context;

import com.roy.spring.framework.annotation.RoyAutowired;
import com.roy.spring.framework.annotation.RoyController;
import com.roy.spring.framework.annotation.RoyService;
import com.roy.spring.framework.bean.BeanDefinition;
import com.roy.spring.framework.bean.BeanPostProcessor;
import com.roy.spring.framework.bean.BeanWrapper;
import com.roy.spring.framework.context.support.BeanDefinitionReader;
import com.roy.spring.framework.core.BeanFactory;
import jdk.nashorn.internal.ir.CallNode;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Roy
 */
public class RoyApplicationContext implements BeanFactory {
    private String[] configLocations;
    private BeanDefinitionReader reader;
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    private Map<String, Object> beanCacheMap = new HashMap<>(); // IOC容器，保证单例
    private Map<String, BeanWrapper> beanWrappedMap = new ConcurrentHashMap<>(); // 被代理过的对象

    public RoyApplicationContext(String... configLocations) {
        this.configLocations  = configLocations;
        refresh();
    }

    public void refresh(){
        // 定位：创建一个Reader，把类的扫描路径封装进去
        this.reader = new BeanDefinitionReader(configLocations);
        // 加载：通过Reader获取指定目录下的所有class文件，封装到一个List<类全路径名>
        List<String> beanDefinitionClasses = reader.loadBeanDefinitions();
        // 注册，把 类简称/类接口类型 与 BeanDefinition 的映射放入到Map中
        doRegistry(beanDefinitionClasses);
        // 处理依赖注入，通过调用getBean()实现
        doAutowire();
    }

    private void doAutowire() {
        beanDefinitionMap.forEach((key,value)->{
            if (!value.getLazyInit()){
                getBean(key);
            }
        });
    }

    // 处理依赖注入
    private void populateBean(String beanName,Object instance){
        Class<?> clazz = instance.getClass();
        if (!(clazz.isAnnotationPresent(RoyController.class)|| clazz.isAnnotationPresent(RoyService.class))){
            return;
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(RoyAutowired.class)) continue;

            String autowiredBeanName = field.getAnnotation(RoyAutowired.class).value().trim();
            if (autowiredBeanName!=null && autowiredBeanName.length()>0){
                field.setAccessible(true);
                try {
                    BeanWrapper beanWrapper = beanWrappedMap.get(autowiredBeanName);
                    if (beanWrapper == null){
                        // 如果依赖的对象还没有初始化，则调用getBean()进行初始化，并得到以一个BeanWrapper对象
                        Object wrappedInstance = getBean(autowiredBeanName);
                        beanWrapper = new BeanWrapper(wrappedInstance);
                        beanWrappedMap.put(autowiredBeanName,beanWrapper);
                    }
                    field.set(instance, beanWrapper.getWrappedInstance());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    // 根据类名，创建对应的BeanDefinition，并保存类简称与BeanDefinition的映射
    // 同时，还保存接口类型（全路径名）与BeanDefinition的映射。放同一个Map中
    private void doRegistry(List<String> beanDefinitionClasses){
        try {
            for (String className : beanDefinitionClasses) {
                Class<?> beanClass = Class.forName(className);
                // 如果是一个接口，则不能实例化
                if (beanClass.isInterface()){continue;}

                BeanDefinition beanDefinition = reader.registerBean(className);
                if (beanDefinition != null){
                    beanDefinitionMap.put(beanDefinition.getFactoryBeanName(),beanDefinition);
                }

                Class<?>[] interfaces = beanClass.getInterfaces();
                for (Class<?> inf : interfaces) {
                    beanDefinitionMap.put(inf.getName(),beanDefinition);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Object getBean(String beanName) {
        System.out.println("=============="+beanName);
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        String className = beanDefinition.getBeanClassName();

        try {
            // bean初始化前后发布通知事件
            BeanPostProcessor beanPostProcessor = new BeanPostProcessor();
            Object instance = initialBean(beanDefinition);
            if (instance == null) return null;

            // 在实例化之前调用一次
            beanPostProcessor.postProcessBeforeInitialization(instance,beanName);

            BeanWrapper beanWrapper = new BeanWrapper(instance);
            beanWrappedMap.put(beanName,beanWrapper);

            // 在实例化之后调用一次
            beanPostProcessor.postProcessAfterInitialization(instance,beanName);

            // 处理依赖注入
            populateBean(beanName,instance);
            return beanWrappedMap.get(beanName).getWrappedInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }
    private Object initialBean(BeanDefinition beanDefinition) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String className = beanDefinition.getBeanClassName();
        if (beanCacheMap.containsKey(className)){
            return  beanCacheMap.get(className);
        }else{
            Class<?> clazz = Class.forName(className);
            Object obj = clazz.newInstance();
            beanCacheMap.put(className,obj);
            return obj;
        }
    }
}
