package com.roy.rmi2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author Roy
 */
public class TcpTransport {
    private String host;
    private int port;

    public TcpTransport(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Object send(RpcReq req) throws Exception {
        Socket socket = new Socket(host,port);
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(req);
        outputStream.flush();

        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        Object result = inputStream.readObject();
        inputStream.close();
        outputStream.close();
        return result;
    }
}
