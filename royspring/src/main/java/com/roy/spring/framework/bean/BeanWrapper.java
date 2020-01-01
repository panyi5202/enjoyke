package com.roy.spring.framework.bean;

import com.roy.spring.framework.core.FactoryBean;

/**
 * @author Roy
 */
public class BeanWrapper extends FactoryBean {
    private Object warpperInstance;
    private Object originalInstance;

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
