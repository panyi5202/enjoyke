package com.roy.rmi2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Roy
 */
public class RemoteInvocationHandler implements InvocationHandler {
    private String host;
    private int port;
    public RemoteInvocationHandler(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcReq rpcReq = new RpcReq();
        rpcReq.setClassName(method.getDeclaringClass().getName());
        rpcReq.setMethodName(method.getName());
        rpcReq.setParameters(args);

        TcpTransport tcpTransport = new TcpTransport(host, port);
        return tcpTransport.send(rpcReq);
    }
}
