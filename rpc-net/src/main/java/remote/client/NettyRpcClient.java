package remote.client;

import exception.RpcException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import remote.channelHandler.DeCoder;
import remote.channelHandler.Encoder;
import remote.dto.RpcMessage;
import remote.dto.RpcRequest;
import remote.dto.RpcResponse;
import remote.handler.SimpleCoder;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class NettyRpcClient {

    private static Bootstrap bootstrap;
    private static Map<String, CompletableFuture<RpcMessage>> completableFutureMap = new ConcurrentHashMap<>();

    static {

        EventLoopGroup eventExecutors = new NioEventLoopGroup();

        bootstrap = new Bootstrap();
        bootstrap.group(eventExecutors)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new DeCoder());
                        socketChannel.pipeline().addLast(new Encoder());
                        socketChannel.pipeline().addLast(new ClientHandler(completableFutureMap));
                    }
                });
    }


    public static RpcResponse request(RpcRequest request, String ip, int port) {
        Channel channel = getChannel(port, ip);

        CompletableFuture<RpcMessage> completableFuture = new CompletableFuture<>();
        completableFutureMap.put(request.getRequestId(), completableFuture);
        RpcMessage requestMessage = SimpleCoder.genMessage(request);

        channel.writeAndFlush(requestMessage).addListener((ChannelFutureListener) listener -> {
            if (listener.isSuccess()) {
                System.out.println("message send success");
            } else {
                Throwable throwable = listener.cause();
                completableFuture.completeExceptionally(throwable);
            }
        });
        try {
            return (RpcResponse) completableFuture.get().getData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Channel connect(int port, String ip) {
        CompletableFuture<Channel> channelCompletableFuture = new CompletableFuture<>();
        bootstrap.connect(ip, port).addListener((ChannelFutureListener) listener -> {
            if (listener.isSuccess()) {
                channelCompletableFuture.complete(listener.channel());
            } else {
                throw new RpcException("连接到服务器错误");
            }
        });

        try {
            return channelCompletableFuture.get();
        } catch (Exception e) {
            throw new RpcException("获取future结果错误");
        }
    }

    // TODO: 2021/3/7 这里有一个问题如果有两个线程同时想向一个主机发送消息怎么处理  等下想一下 这个要自己处理了别再看了
    public static Channel getChannel(int port, String ip) {
        Channel channel = ChannelManager.get(ip + port);

        if (channel == null) {
            channel = connect(port, ip);
            ChannelManager.addChannel(ip + port, channel);
        }

        return channel;

    }

}
