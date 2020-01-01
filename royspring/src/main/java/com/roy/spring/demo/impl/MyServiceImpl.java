package com.roy.spring.demo.impl;

import com.roy.spring.demo.MyService;
import com.roy.spring.framework.annotation.RoyService;

/**
 * @author Roy
 */
@RoyService("myServiceImpl")
public class MyServiceImpl implements MyService {
    @Override
    public String info(String msg) {
        return "info="+msg;
    }
}
