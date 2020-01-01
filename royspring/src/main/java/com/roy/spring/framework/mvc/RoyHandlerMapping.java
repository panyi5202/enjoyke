package com.roy.spring.framework.mvc;

import com.roy.spring.framework.annotation.RoyController;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author Roy
 */
public class RoyHandlerMapping {
    private Pattern url;
    private Object controller;
    private Method method;

    public RoyHandlerMapping(Pattern url, Object controller, Method method) {
        this.url = url;
        this.controller = controller;
        this.method = method;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Pattern getUrl() {
        return url;
    }

    public void setUrl(Pattern url) {
        this.url = url;
    }
}
