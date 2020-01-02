package com.roy.spring.framework.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Roy
 */
public class RoyHandlerAdapter {
    private RoyHandlerMapping handlerMapping;
    private Map<String, Integer> paramMapping;

    public RoyHandlerAdapter(RoyHandlerMapping handlerMapping, Map<String, Integer> paramMapping) {
        this.handlerMapping = handlerMapping;
        this.paramMapping = paramMapping;
    }

    public RoyModeAndView handle(HttpServletRequest req, HttpServletResponse resp, RoyHandlerMapping handler) throws InvocationTargetException, IllegalAccessException {
        // 准备这个方法的形参列表
        Class<?>[] methodParamTypes = handler.getMethod().getParameterTypes();
        // 拿到request中传过来的参数列表
        Map<String, String[]> reqParamMap = req.getParameterMap();
        // 构造实参列表
        Object[] paramValues = new Object[methodParamTypes.length];
        for (Map.Entry<String, String[]> entry : reqParamMap.entrySet()) {
            String value = Arrays.toString(entry.getValue())
                    .replaceAll("\\[|\\]", "").replaceAll("\\s", ",");

            if (paramMapping.containsKey(entry.getKey())) {
                int index = paramMapping.get(entry.getKey());
                paramValues[index] = caseStringValue(value, methodParamTypes[index]);
            }
        }
        // 把httpServletRequest和HttpServletResponse也放发到实参列表中

        if (paramMapping.containsKey(HttpServletRequest.class.getName())) {
            Integer reqIndex = paramMapping.get(HttpServletRequest.class.getName());
            paramValues[reqIndex] = req;
        }
        if (paramMapping.containsKey(HttpServletResponse.class.getName())) {
            Integer respIndex = paramMapping.get(HttpServletResponse.class.getName());
            paramValues[respIndex] = resp;
        }
        // cong handler中获取controller、method，然后利用反射机制调用
        Object result = handler.getMethod().invoke(handler.getController(), paramValues);
        if (result != null) {
            boolean isModelAndView = handler.getMethod().getReturnType().equals(RoyModeAndView.class);
            if (isModelAndView) {
                return (RoyModeAndView) result;
            }
        }
        return null;
    }

    private Object caseStringValue(String value, Class<?> clazz) {
        if (clazz.equals(String.class)) {
            return value;
        } else if (clazz.equals(Integer.class)) {
            return Integer.valueOf(value);
        } else if (clazz.equals(int.class)) {
            return Integer.valueOf(value).intValue();
        } else return null;
    }
}
