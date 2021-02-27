package remote.dto;

public class RpcHeader {

    private byte[] magicNum;

    private byte version;

    private int dataLength;

    private byte messageType;

    private byte compressType;

    private byte serializeType;


    public RpcHeader setMagicNum(byte[] magicNum) {
        this.magicNum = magicNum;
        return this;
    }

    public RpcHeader setVersion(byte version) {
        this.version = version;
        return this;
    }

    public RpcHeader setDataLength(int dataLength) {
        this.dataLength = dataLength;
        return this;
    }

    public RpcHeader setMessageType(byte messageType) {
        this.messageType = messageType;
        return this;
    }

    public RpcHeader setCompressType(byte compressType) {
        this.compressType = compressType;
        return this;
    }

    public RpcHeader setSerializeType(byte serializeType) {
        this.serializeType = serializeType;
        return this;
    }


    public byte[] getMagicNum() {
        return magicNum;
    }

    public byte getVersion() {
        return version;
    }

    public int getDataLength() {
        return dataLength;
    }

    public byte getMessageType() {
        return messageType;
    }

    public byte getCompressType() {
        return compressType;
    }

    public byte getSerializeType() {
        return serializeType;
    }
}
