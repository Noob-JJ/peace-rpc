package remote.server;

import remote.handler.RequestHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * Created by JackJ on 2021/1/16.
 */
public class RpcServer implements Server{

    private int port;

    private RequestHandler requestHandler;


    public RpcServer(int port, RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
        this.port = port;
    }

    private static final ExecutorService threadPool =
            new ThreadPoolExecutor(10, 100, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100), Executors.defaultThreadFactory());



    public void start(){
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                threadPool.execute(() -> {
                    try {
                        requestHandler.handler(socket.getInputStream(), socket.getOutputStream());
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
