package com.roy.spring.demo;

import com.roy.spring.framework.annotation.RoyAutowired;
import com.roy.spring.framework.annotation.RoyController;
import com.roy.spring.framework.annotation.RoyRequestMapping;
import com.roy.spring.framework.annotation.RoyRequestParam;
import com.roy.spring.framework.mvc.RoyModeAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Roy
 */
@RoyRequestMapping("/my")
@RoyController("myAction")
public class MyAction {
    @RoyAutowired("myServiceImpl")
    private MyService myService;

    @RoyRequestMapping("/info")
    public RoyModeAndView info(HttpServletRequest request, HttpServletResponse response,
                               @RoyRequestParam("msg") String msg, @RoyRequestParam("id") Integer id) {
        String info = myService.info(msg);
        System.out.println(info);
        try {
            response.getWriter().write(info);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RoyRequestMapping("/first.html")
    public RoyModeAndView page(HttpServletRequest request, HttpServletResponse response,
                               @RoyRequestParam("msg") String msg) {
        Map<String, Object> model = new HashMap<>();
        model.put("msg", msg);
        model.put("data", "12345");
        return new RoyModeAndView("first.html", model);
    }
}
