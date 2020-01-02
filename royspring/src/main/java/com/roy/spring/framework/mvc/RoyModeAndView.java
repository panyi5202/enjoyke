package com.roy.spring.framework.mvc;

import java.util.Map;

/**
 * @author Roy
 */
public class RoyModeAndView {
    private String viewName;
    private Map<String, ?> model;
    public RoyModeAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }

    public void setModel(Map<String, ?> model) {
        this.model = model;
    }
}
