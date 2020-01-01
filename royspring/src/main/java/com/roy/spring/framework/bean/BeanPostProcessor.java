package com.roy.spring.framework.bean;

import com.sun.corba.se.impl.ior.OldJIDLObjectKeyTemplate;

/**
 * @author Roy
 */
public class BeanPostProcessor {
    public Object postProcessBeforeInitialization(Object bean,String beanName){
        return bean;
    }
    public Object postProcessAfterInitialization(Object bean,String beanName){
        return bean;
    }
}
