package remote.server;

import common.RpcConstant;
import remote.dto.RpcHeader;
import remote.dto.RpcMessage;
import remote.dto.RpcRequest;
import remote.dto.RpcResponse;
import remote.handler.RequestHandler;
import remote.handler.SimpleCoder;
import util.ByteUtils;
import util.compress.CompressUtil;
import util.serialize.KryoUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * Created by JackJ on 2021/1/16.
 */
public class RpcServer implements Server {

    private int port;

    private RequestHandler requestHandler;


    public RpcServer(int port, RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
        this.port = port;
    }

    private static final ExecutorService threadPool =
            new ThreadPoolExecutor(10, 100, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100), Executors.defaultThreadFactory());


    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                threadPool.execute(() -> {
                    try (InputStream inputStream = socket.getInputStream();
                         OutputStream outputStream = socket.getOutputStream()) {

                        RpcMessage requestMessage = SimpleCoder.decode(inputStream);

                        RpcResponse response = requestHandler.handler(requestMessage);
                        RpcMessage responseMessage = SimpleCoder.genMessage(response);

                        outputStream.write(SimpleCoder.encode(responseMessage));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
