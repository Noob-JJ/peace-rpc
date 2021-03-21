package exception;

public class RpcException extends RuntimeException {

    public RpcException(){
        super();
    }

    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public RpcException(Throwable throwable) {
        super(throwable);
    }
}
