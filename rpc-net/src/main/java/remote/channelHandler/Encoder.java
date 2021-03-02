package remote.channelHandler;

import common.RpcConstant;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import remote.dto.RpcHeader;
import remote.dto.RpcMessage;
import util.compress.CompressUtil;
import util.serialize.KryoUtils;

import static common.RpcConstant.*;

public class Encoder extends MessageToByteEncoder<RpcMessage> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage, ByteBuf byteBuf) {

        RpcHeader header = rpcMessage.getHeader();

        if (header.getMessageType() == MESSAGE_TYPE_REQUEST || header.getMessageType() == MESSAGE_TYPE_RESPONSE) {
            byte[] data = CompressUtil.compress(new KryoUtils().serialize(rpcMessage.getData()), header.getCompressType());
            header.setDataLength(data.length);
            puddingBuffer(byteBuf, header, data);
        }

        // TODO: 2021/2/27 等之后消息类型变多了之后可能实现逻辑需要更改 
    }

    private void puddingBuffer(ByteBuf byteBuf, RpcHeader header, byte[] data) {

        byteBuf.writeBytes(header.getMagicNum());
        byteBuf.writeByte(header.getVersion());
        byteBuf.writeInt(header.getDataLength());
        byteBuf.writeByte(header.getMessageType());
        byteBuf.writeByte(header.getCompressType());
        byteBuf.writeByte(header.getSerializeType());

        byteBuf.writeBytes(data);
    }
}
