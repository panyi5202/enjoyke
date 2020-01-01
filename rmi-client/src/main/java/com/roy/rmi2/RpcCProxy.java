package com.roy.rmi2;

import java.lang.reflect.Proxy;

/**
 * @author Roy
 */
public class RpcCProxy {
    public static <T> T createProxy(Class<T> t,String host,int port) {

        return (T)Proxy.newProxyInstance(t.getClassLoader(),new Class[]{t},
                new RemoteInvocationHandler(host,port));
    }
}
