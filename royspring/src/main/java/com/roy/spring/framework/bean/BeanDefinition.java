package com.roy.spring.framework.bean;

/**
 * @author Roy
 */
public class BeanDefinition {
    private String beanClassName;
    private String FactoryBeanName;
    private Boolean lazyInit = false;

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public String getFactoryBeanName() {
        return FactoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        FactoryBeanName = factoryBeanName;
    }

    public Boolean getLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(Boolean lazyInit) {
        this.lazyInit = lazyInit;
    }
}
