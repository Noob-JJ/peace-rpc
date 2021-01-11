package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * rpc网络调用类
 * Created by JackJ on 2021/1/11.
 */
public class RpcClient {

    private String host;

    private Integer port;

    private static final int TIMEOUT_TIME = 9 * 1000;

    public byte[] request(byte[] data) {
        try(Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(TIMEOUT_TIME);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            outputStream.write(data);
            inputStream.read();
            // TODO: 2021/1/11 接受数据的逻辑需要再思考下 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
