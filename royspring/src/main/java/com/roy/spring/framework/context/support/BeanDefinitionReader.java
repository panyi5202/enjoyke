package com.roy.spring.framework.context.support;

import com.roy.spring.framework.bean.BeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Roy
 */
public class BeanDefinitionReader {
    private Properties config = new Properties();
    private List<String> registryBeanClasses = new ArrayList<>();

    public BeanDefinitionReader(String... configLocations) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(
                configLocations[0].replace("classpath:", ""));
        try {
            config.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        doScanner(config.getProperty("scanPackage"));
    }

    public List<String> loadBeanDefinitions() {
        return this.registryBeanClasses;
    }

    public BeanDefinition registerBean(String className) {
        if(registryBeanClasses.contains(className)){
            BeanDefinition beanDefinition = new BeanDefinition();
            beanDefinition.setBeanClassName(className);
            // 这个FactoryBeanName是类名的首字母小写，比如：myAction
            beanDefinition.setFactoryBeanName(lowerFirstCase(className.substring(className.lastIndexOf(".")+1)));
            return beanDefinition;
        }
        return null;
    }

    public void doScanner(String packageName) {
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
        File classDir = new File(url.getFile());
        for (File file : classDir.listFiles()) {
            if (file.isDirectory()) {
                doScanner(packageName + "." + file.getName());
            } else {
                registryBeanClasses.add(packageName + "." + file.getName().replace(".class",""));
            }
        }
        System.out.println();
    }

    public Properties getConfig() {
        return config;
    }

    private String lowerFirstCase(String str){
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
