package remote.dto;

import java.io.Serializable;

/**
 * Created by JackJ on 2021/1/16.
 */
// TODO: 2021/2/21 这里可以优化一些 添加静态方法 success fail exception这样更加直观一点
public class RpcResponse implements Serializable {

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
}
