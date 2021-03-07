package remote.handler;

import remote.dto.RpcMessage;
import remote.dto.RpcRequest;
import remote.dto.RpcResponse;
import remote.handler.RequestHandler;
import util.serialize.SerializeFactory;

import java.io.*;
import java.lang.reflect.Method;

/**
 * Created by JackJ on 2021/1/16.
 */
public class SimpleRequestHandler implements RequestHandler {

    @Override
    public RpcResponse handler(RpcMessage rpcMessage) throws IOException {
        RpcResponse response = new RpcResponse();
        try {
            RpcRequest request = (RpcRequest) rpcMessage.getData();

            Class<?> cls = Class.forName(request.getClassName());
            Method method = checkoutIfExistMethod(request.getMethodName(), cls);
            Object obj = cls.newInstance();
            Object result = method.invoke(obj, request.getParams());

            response.setRequestId(request.getRequestId());
            response.setHasException(false);
            response.setResult(result);
        } catch (Exception e) {
            response.setHasException(true);
            response.setResult(e);
        }

        return response;
    }

    private RpcRequest readData(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024 * 1024];

        int length = inputStream.read(buffer);

        byte[] result = new byte[length];
        System.arraycopy(buffer, 0, result, 0, length);

        return SerializeFactory.getSerializeUtil().deserialize(RpcRequest.class, result);
    }

    private Method checkoutIfExistMethod(String methodName, Class<?> cls) throws Exception {
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        throw new Exception("not found method:" + methodName);
    }
}
