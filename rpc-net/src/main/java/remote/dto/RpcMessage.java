package remote.dto;

public class RpcMessage {

    public RpcMessage(RpcHeader header, Object data) {
        this.header = header;
        this.data = data;
    }

    private RpcHeader header;

    private Object data;

    public Object getData() {
        return data;
    }

    public RpcHeader getHeader() {
        return header;
    }
}
