package com.roy.rmi;

import java.lang.reflect.Proxy;

/**
 * @author Roy
 */
public class RpcClientProxy {
    public <T> T clientProxy(final Class<T> interfaceCls,final String host,final int port){
        return (T)Proxy.newProxyInstance(interfaceCls.getClassLoader(),new Class[]{interfaceCls},
                new RemoteInvocatHandler(host,port));
    }
}
