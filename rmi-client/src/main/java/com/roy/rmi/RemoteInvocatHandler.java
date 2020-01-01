package com.roy.rmi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Roy
 */
public class RemoteInvocatHandler   implements InvocationHandler {
    private String host;
    private int port;

    public RemoteInvocatHandler(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameters(args);

        TcpTransport tcpTransport = new TcpTransport(host, port);
        return tcpTransport.send(rpcRequest);
    }
}
