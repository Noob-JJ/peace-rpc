package common;

public class RpcConstant {

    public static final byte[] PROTOCOL_HEADER_MAGIC_NUM = new byte[]{'0', '3', '3', '3'};
    public static final byte PROTOCOL_HEADER_VERSION = (byte) 1;
    public static final int PROTOCOL_MAX_LENGTH = 30 * 1024 * 1024;

    public static final int MESSAGE_TYPE_DATA = 0;
    public static final int MESSAGE_TYPE_REQUEST = 1;
    public static final int MESSAGE_TYPE_RESPONSE = 2;
}
