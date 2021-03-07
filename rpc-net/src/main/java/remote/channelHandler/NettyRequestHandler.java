package remote.channelHandler;

import common.RpcConstant;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;
import remote.dto.RpcMessage;
import remote.dto.RpcResponse;
import remote.handler.RequestHandler;
import remote.handler.SimpleCoder;

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

                RpcResponse response = requestHandler.handler(message);
                RpcMessage responseMessage = SimpleCoder.genMessage(response);

                ctx.writeAndFlush(responseMessage).addListener((ChannelFutureListener) future -> {
                    if (!future.isSuccess()) {
                        System.out.println("error");
                        try {
                            throw future.cause();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                });
            }
        }
        ReferenceCountUtil.release(msg);
    }
}
