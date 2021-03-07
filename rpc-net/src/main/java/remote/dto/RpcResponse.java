package remote.dto;

import java.io.Serializable;

/**
 * Created by JackJ on 2021/1/16.
 */
public class RpcResponse implements Serializable {

    private String requestId;

    private boolean hasException;

    private Object result;

    public Object get() throws Exception {
        if (hasException) {
            throw (Exception) result;
        } else {
            return result;
        }
    }

    public void setHasException(boolean hasException) {
        this.hasException = hasException;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
