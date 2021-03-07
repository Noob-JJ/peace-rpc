package remote.handler;

import common.RpcConstant;
import remote.dto.RpcHeader;
import remote.dto.RpcMessage;
import remote.dto.RpcRequest;
import remote.dto.RpcResponse;
import util.ByteUtils;
import util.compress.CompressUtil;
import util.serialize.KryoUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class SimpleCoder {

    public static RpcMessage decode(InputStream inputStream) throws IOException {

        RpcHeader rpcHeader = analysisHeader(inputStream);
        verifyHeader(rpcHeader);

        byte[] dataBytes = new byte[rpcHeader.getDataLength()];
        read(inputStream, dataBytes);

        Object data = deserializeAndUnzip(rpcHeader, dataBytes);

        return new RpcMessage(rpcHeader, data);
    }

    public static byte[] encode(RpcMessage message) {

        byte[] data = compressAndSerialize(message);
        message.getHeader().setDataLength(data.length);

        byte[] headerBytes = getHeaderByte(message.getHeader());

        return paddingByteArray(headerBytes, data);
    }

    public static byte[] paddingByteArray(byte[] header, byte[] data) {

        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[header.length + data.length]);

        byteBuffer.put(header);
        byteBuffer.put(data);

        return byteBuffer.array();
    }

    public static RpcMessage genMessage(Object data) {

        RpcHeader rpcHeader = new RpcHeader();
        rpcHeader.setMessageType(RpcConstant.DEFAULT_SERIALIZE_TYPE)
                .setCompressType(RpcConstant.DEFAULT_COMPRESS_TYPE)
                .setVersion(RpcConstant.PROTOCOL_HEADER_VERSION)
                .setMagicNum(RpcConstant.PROTOCOL_HEADER_MAGIC_NUM)
                .setDataLength(-1)
                .setMessageType(data instanceof RpcRequest ? RpcConstant.MESSAGE_TYPE_REQUEST :
                        RpcConstant.MESSAGE_TYPE_RESPONSE);


        return new RpcMessage(rpcHeader, data);
    }

    private static void verifyHeader(RpcHeader header) {
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

    private static byte[] compressAndSerialize(RpcMessage rpcMessage) {

        return CompressUtil.compress(new KryoUtils().serialize(rpcMessage.getData()), rpcMessage.getHeader().getCompressType());
    }

    private static byte[] getHeaderByte(RpcHeader header) {

        byte[] headerBytes = new byte[12];
        int position = 0;

        System.arraycopy(header.getMagicNum(), 0, headerBytes, 0, RpcConstant.PROTOCOL_HEADER_MAGIC_NUM.length);
        position += RpcConstant.PROTOCOL_HEADER_MAGIC_NUM.length;
        headerBytes[position++] = header.getVersion();
        System.arraycopy(ByteUtils.intToBytes(header.getDataLength()), 0, headerBytes, position, 4);
        position += 4;
        headerBytes[position++] = header.getMessageType();
        headerBytes[position++] = header.getCompressType();
        headerBytes[position] = header.getSerializeType();

        return headerBytes;
    }

    private static RpcHeader analysisHeader(InputStream inputStream) throws IOException {
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
                .setMagicNum(magicNum)
                .setSerializeType(serializeType)
                .setVersion(version)
                .setDataLength(ByteUtils.bytesToInt(dataLength))
                .setCompressType(compressType)
                .setMessageType(messageType);
    }

    private static void read(InputStream inputStream, byte[] buffer) {

        try {
            int size = inputStream.read(buffer);
            while (size != buffer.length) {
                size += inputStream.read(buffer, size, buffer.length - size);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static Object deserializeAndUnzip(RpcHeader header, byte[] data) {

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
