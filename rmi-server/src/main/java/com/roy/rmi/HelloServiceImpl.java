package com.roy.rmi;

/**
 * @author Roy
 */
public class HelloServiceImpl implements IHelloService {
    public String sayHello(String msg) {
        return "Roy," + msg;
    }
}
