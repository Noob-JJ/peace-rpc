package remote.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import remote.handler.RequestHandler;

import java.net.InetSocketAddress;

import static util.RuntimeUtil.*;

public class NettyServer implements Server {

    private final int port;

    private final RequestHandler requestHandler;


    public NettyServer(int port, RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
        this.port = port;
    }

    @Override
    public void start() {
        DefaultEventLoopGroup businessThreadPool = new DefaultEventLoopGroup(getCpuNum());

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            //sc.pipeline().addLast();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            businessThreadPool.shutdownGracefully();
        }
    }
}
