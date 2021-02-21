package net.client;

import com.esotericsoftware.kryo.Kryo;
import net.entity.RpcRequest;
import net.entity.RpcResponse;
import util.KryoUtils;

import java.io.*;
import java.net.Socket;

/**
 * rpc网络调用类
 * Created by JackJ on 2021/1/11.
 */
public class RpcClient {

    private static final int TIMEOUT_TIME = 9 * 1000;

    public static RpcResponse request(RpcRequest rpcRequest, String host, int port) {
        byte[] buffer = new byte[1024 * 1024];

        try (Socket socket = new Socket(host, port);
             OutputStream outputStream = socket.getOutputStream();
             InputStream inputStream = socket.getInputStream()) {
            socket.setSoTimeout(TIMEOUT_TIME);

            byte[] request = KryoUtils.serialize(rpcRequest);
            outputStream.write(request);
            int length = inputStream.read(buffer);

            byte[] result = new byte[length];
            System.arraycopy(buffer, 0, result, 0, length);

            return KryoUtils.deserialize(RpcResponse.class, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
