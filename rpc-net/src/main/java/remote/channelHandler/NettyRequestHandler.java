package remote.channelHandler;

import common.RpcConstant;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.DefaultEventLoopGroup;
import remote.dto.RpcMessage;
import remote.handler.RequestHandler;

public class NettyRequestHandler extends ChannelInboundHandlerAdapter {

    private final RequestHandler requestHandler;


    public NettyRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RpcMessage) {
            RpcMessage message = (RpcMessage) msg;

            if (message.getHeader().getMessageType() == RpcConstant.MESSAGE_TYPE_REQUEST) {
                requestHandler.handler(message);
            }
        }
    }
}
