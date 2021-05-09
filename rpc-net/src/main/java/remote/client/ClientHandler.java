package remote.client;

import exception.RpcException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import remote.dto.RpcMessage;
import remote.dto.RpcResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ClientHandler extends SimpleChannelInboundHandler<RpcMessage> {

    private final Map<String, CompletableFuture<RpcMessage>> map;

    public ClientHandler(Map<String, CompletableFuture<RpcMessage>> map) {
        this.map = map;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage) {
        CompletableFuture<RpcMessage> result = map.get(((RpcResponse) rpcMessage.getData()).getRequestId());

        if (result == null) {
            throw new RpcException("无效响应");
        }

        result.complete(rpcMessage);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.ALL_IDLE)) {
                // TODO-peace: 2021/5/9 确定channel中的地址形式是否满足我的manager中key形式
                String address = ctx.channel().remoteAddress().toString();
                ChannelManager.removeChannel(address);

            }
        }

        ctx.fireUserEventTriggered(evt);
    }
}
