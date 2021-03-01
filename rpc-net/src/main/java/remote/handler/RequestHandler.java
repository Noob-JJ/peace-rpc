package remote.handler;

import remote.dto.RpcMessage;
import remote.dto.RpcResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by JackJ on 2021/1/16.
 */
public interface RequestHandler {

    RpcResponse handler(RpcMessage rpcMessage) throws IOException;
}