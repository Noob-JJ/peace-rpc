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

public class SimpleDecoder {

    public RpcMessage decode(InputStream inputStream) throws IOException {

        RpcHeader rpcHeader = analysisHeader(inputStream);
        byte[] dataBytes = new byte[rpcHeader.getDataLength()];
        read(inputStream, dataBytes);

        Object data = deserializeAndUnzip(rpcHeader, dataBytes);

        return new RpcMessage(rpcHeader, data);
    }

    public byte[] encode(RpcMessage message) {
        // TODO: 2021/3/1 待做
        return null;
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
