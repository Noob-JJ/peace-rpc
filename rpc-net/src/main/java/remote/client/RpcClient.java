package remote.client;

import common.RpcConstant;
import remote.dto.RpcHeader;
import remote.dto.RpcMessage;
import remote.dto.RpcRequest;
import remote.dto.RpcResponse;
import remote.handler.SimpleCoder;
import util.serialize.SerializeFactory;

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

            RpcMessage requestMessage = SimpleCoder.genMessage(rpcRequest);
            byte[] requestBytes = SimpleCoder.encode(requestMessage);

            outputStream.write(requestBytes);

            RpcMessage responseMessage = SimpleCoder.decode(inputStream);

            return (RpcResponse) responseMessage.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
