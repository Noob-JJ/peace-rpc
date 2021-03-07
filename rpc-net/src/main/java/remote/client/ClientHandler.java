package remote.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
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
            throw new RuntimeException("无效响应");
        }

        result.complete(rpcMessage);
    }
}
