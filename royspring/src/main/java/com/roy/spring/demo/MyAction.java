package com.roy.spring.demo;

import com.roy.spring.framework.annotation.RoyAutowired;
import com.roy.spring.framework.annotation.RoyController;

/**
 * @author Roy
 */
@RoyController("myAction")
public class MyAction {
    @RoyAutowired("myServiceImpl")
    private MyService myService;

    public void info(String msg){
        String info = myService.info(msg);
        System.out.println(info);
    }
}
