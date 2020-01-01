package com.roy.spring.framework.mvc;

import java.io.File;

/**
 * @author Roy
 * 将静态文件中的参数表达式解析成对应的值
 */
public class RoyViewResolver {
    private String viewName;
    private File templateFile;

    public RoyViewResolver(String viewName, File tempFile) {
        this.viewName = viewName;
        this.templateFile = tempFile;
    }

    public String resolverView(RoyModeAndView mv) {
        return null;
    }
}
