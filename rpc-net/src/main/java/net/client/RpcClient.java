package net.client;

import net.entity.RpcRequest;

import java.io.*;
import java.net.Socket;

/**
 * rpc网络调用类
 * Created by JackJ on 2021/1/11.
 */
public class RpcClient {

    private String host;

    private Integer port;

    private static final int TIMEOUT_TIME = 9 * 1000;

    public Object request(RpcRequest rpcRequest) {
        try(Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(TIMEOUT_TIME);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(rpcRequest);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            return objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
