package com.roy.rmi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @author Roy
 */
public class ProcessorHandler implements Runnable {
    private Socket socket;
    private  Object service;

    public ProcessorHandler(Socket socket, Object service) {
        this.socket = socket;
        this.service = service;
    }

    public void run() {
        //TODO 处理请求
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            RpcRequest rpcRequest = (RpcRequest) inputStream.readObject();

            Object result = invoke(rpcRequest);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();
            inputStream.close();
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
    // 通过反射调用远程传输过来的RpcRequest对象的方法
    private Object invoke(RpcRequest request) throws Exception {
        Object[] args = request.getParameters();
        Class[] types = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            types[i] = args[i].getClass();
        }
        Method method = service.getClass().getMethod(request.getMethodName(), types);
        return method.invoke(service,args);
    }
}
