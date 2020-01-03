package com.roy.spring.framework.bean;

import com.roy.spring.framework.core.FactoryBean;

/**
 * @author Roy
 */
public class BeanWrapper extends FactoryBean {
    private Object warpperInstance; // 原始类的代理对象
    private Object originalInstance; // 原始对象

    private BeanPostProcessor beanPostProcessor;

    public BeanWrapper(Object instance) {
        this.warpperInstance = instance;
        this.originalInstance = instance;
    }

    public Object getWrappedInstance(){
        return warpperInstance;
    }
    public Class<?> getWarppedClass(){
        return warpperInstance.getClass();
    }

    public BeanPostProcessor getBeanPostProcessor() {
        return beanPostProcessor;
    }

    public void setBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessor = beanPostProcessor;
    }
}
