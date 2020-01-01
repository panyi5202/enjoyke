package com.roy.rmi;

import java.io.IOException;

/**
 * @author Roy
 */
public class ServerDemo {
    public static void main(String[] args) {
        try {
            IHelloService service = new HelloServiceImpl();
            RpcServer rpcServer = new RpcServer();
            rpcServer.publisher(service,8888);
            System.out.println("服务发布成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
