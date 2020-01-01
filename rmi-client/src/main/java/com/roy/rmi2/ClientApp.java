package com.roy.rmi2;

/**
 * @author Roy
 */
public class ClientApp {
    public static void main(String[] args) {
        IByeService byeService = RpcCProxy.createProxy(IByeService.class,"localhost",9999);
        System.out.println(byeService.bye("rrroy"));
    }
}
