package com.roy.rmi;

/**
 * @author Roy
 */
public class ClientDemo {
    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy();
        IHelloService service = proxy.clientProxy(IHelloService.class,"localhost",8888);
        System.out.println(service.sayHello("roy hello"));
    }
}
