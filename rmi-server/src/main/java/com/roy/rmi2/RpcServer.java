package com.roy.rmi2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Roy
 */
public class RpcServer {
    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    public void publishService(final Object service,int port) throws Exception {
        ServerSocket serverSocket = new ServerSocket(port);
        while (true){
            Socket socket = serverSocket.accept();
            executorService.execute(()->{
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                    RpcReq req = (RpcReq)inputStream.readObject();
                    Object[] parameters = req.getParameters();
                    Class[] types = new Class[parameters.length];
                    for (int i = 0; i < parameters.length; i++) {
                        types[i] = parameters[i].getClass();
                    }
                    Method method = service.getClass().getMethod(req.getMethodName(), types);
                    Object result = method.invoke(service, parameters);

                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                    outputStream.writeObject(result);
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }

            });
        }
    }
}
