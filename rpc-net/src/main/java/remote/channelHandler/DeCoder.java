package remote.channelHandler;

import common.RpcConstant;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import remote.dto.RpcHeader;
import remote.dto.RpcMessage;
import remote.dto.RpcRequest;
import remote.dto.RpcResponse;
import util.compress.CompressUtil;
import util.serialize.KryoUtils;

public class DeCoder extends LengthFieldBasedFrameDecoder {

    public DeCoder() {

        this(RpcConstant.PROTOCOL_MAX_LENGTH, 5, 4, 3, 0);

    }


    private DeCoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialByteToStrip) {

        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialByteToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        System.out.println("decode");
        ByteBuf decoded = (ByteBuf) super.decode(ctx, in);
        RpcHeader header = getDecodeHeader(decoded);

        verifyHeader(header);

        byte[] data = new byte[header.getDataLength()];
        decoded.readBytes(data);
        Object result = deserializeAndUnzip(header, data);

        return new RpcMessage(header, result);
    }

    private void verifyHeader(RpcHeader header) {
        byte[] magicNumber = header.getMagicNum();
        for (int i = 0; i < magicNumber.length; i++) {
            if (header.getMagicNum()[i] != RpcConstant.PROTOCOL_HEADER_MAGIC_NUM[i]) {
                throw new RuntimeException("magic error");
            }
        }

        if (header.getVersion() != RpcConstant.PROTOCOL_HEADER_VERSION) {
            throw new RuntimeException("version error");
        }
    }

    private RpcHeader getDecodeHeader(ByteBuf byteBuf) {

        byte[] magicNumber = new byte[RpcConstant.PROTOCOL_HEADER_MAGIC_NUM.length];
        byteBuf.readBytes(magicNumber);
        byte version = byteBuf.readByte();
        int dataLength = byteBuf.readInt();
        byte messageType = byteBuf.readByte();
        byte compressType = byteBuf.readByte();
        byte serializeType = byteBuf.readByte();

        return new RpcHeader()
                .setMagicNum(magicNumber)
                .setDataLength(dataLength)
                .setCompressType(compressType)
                .setMessageType(messageType)
                .setVersion(version)
                .setSerializeType(serializeType);

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
