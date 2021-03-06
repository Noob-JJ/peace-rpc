package common;

public class RpcConstant {

    public static final byte[] PROTOCOL_HEADER_MAGIC_NUM = new byte[]{'0', '3', '3', '3'};
    public static final byte PROTOCOL_HEADER_VERSION = (byte) 1;
    public static final int PROTOCOL_MAX_LENGTH = 30 * 1024 * 1024;

    public static final byte MESSAGE_TYPE_DATA = 0;
    public static final byte MESSAGE_TYPE_REQUEST = 1;
    public static final byte MESSAGE_TYPE_RESPONSE = 2;

    public static final byte DEFAULT_SERIALIZE_TYPE = 0;
    public static final byte DEFAULT_COMPRESS_TYPE = 0;

    public static final String ROOT_NODE_REGISTRY = "/rpc";

}
