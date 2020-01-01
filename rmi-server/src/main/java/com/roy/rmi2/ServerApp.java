package com.roy.rmi2;

/**
 * @author Roy
 */
public class ServerApp {
    public static void main(String[] args) throws Exception {
        IByeService byeService = new ByeServiceImpl();
        // 发布对象
        RpcServer rpcServer = new RpcServer();
        rpcServer.publishService(byeService,9999);
    }
}
