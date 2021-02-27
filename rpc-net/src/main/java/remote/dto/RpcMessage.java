package remote.dto;

public class RpcMessage {

    public Object getDate() {
        return date;
    }

    public RpcHeader getHeader() {
        return header;
    }

    private RpcHeader header;

    private Object date;

}
