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
                        RpcMessage rpcMessage = new SimpleCoder().decode(inputStream);
                        requestHandler.handler(rpcMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private RpcMessage analysisProtocol(InputStream inputStream) throws IOException {

        RpcHeader rpcHeader = analysisHeader(inputStream);
        byte[] dataBytes = new byte[rpcHeader.getDataLength()];
        read(inputStream, dataBytes);

        Object data = deserializeAndUnzip(rpcHeader, dataBytes);

        return new RpcMessage(rpcHeader, data);
    }

    private RpcHeader analysisHeader(InputStream inputStream) throws IOException {
        byte[] magicNum = new byte[RpcConstant.PROTOCOL_HEADER_MAGIC_NUM.length];
        byte version;
        byte[] dataLength = new byte[4];
        byte messageType;
        byte compressType;
        byte serializeType;

        read(inputStream, magicNum);
        version = (byte) inputStream.read();
        read(inputStream, dataLength);
        messageType = (byte) inputStream.read();
        compressType = (byte) inputStream.read();
        serializeType = (byte) inputStream.read();

        return new RpcHeader()
                .setSerializeType(serializeType)
                .setVersion(version)
                .setDataLength(ByteUtils.bytesToInt(dataLength))
                .setCompressType(compressType)
                .setMessageType(messageType);
    }

    private void read(InputStream inputStream, byte[] buffer) {

        try {

            int size = inputStream.read(buffer);
            while (size != buffer.length) {
                size += inputStream.read(buffer, size, buffer.length - size);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Object deserializeAndUnzip(RpcHeader header, byte[] data) {

        byte[] unZipData = CompressUtil.unZip(data, header.getCompressType());

        if (header.getMessageType() == RpcConstant.MESSAGE_TYPE_REQUEST) {
            return new KryoUtils().deserialize(RpcRequest.class, unZipData);
        } else if (header.getMessageType() == RpcConstant.MESSAGE_TYPE_RESPONSE) {
            return new KryoUtils().deserialize(RpcResponse.class, unZipData);
        } else {
            return null;
        }
    }
}
