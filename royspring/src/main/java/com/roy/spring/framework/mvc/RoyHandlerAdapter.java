package com.roy.spring.framework.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Roy
 */
public class RoyHandlerAdapter {
    private RoyHandlerMapping handlerMapping;
    private Map<String,Integer> paramMapping;

    public RoyHandlerAdapter(RoyHandlerMapping handlerMapping,Map<String,Integer> paramMapping) {
        this.handlerMapping = handlerMapping;
        this.paramMapping = paramMapping;
    }

    public RoyModeAndView handle(HttpServletRequest req, HttpServletResponse resp, RoyHandlerMapping handler) {
        // 准备这个方法的形参列表
        Class<?>[] parameterTypes = handler.getMethod().getParameterTypes();
        // 拿到request中传过来的参数列表
        Map<String, String[]> parameterMap = req.getParameterMap();
        // 构造实参列表
        Object[] args = new Object[parameterTypes.length];
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String value = Arrays.toString(entry.getValue())
                    .replaceAll("\\[|\\]", "").replaceAll("\\s", ",");

            if (paramMapping.containsKey(entry.getKey())){
                int index = paramMapping.get(entry.getKey());
            }
        }
        return null;
    }
}
