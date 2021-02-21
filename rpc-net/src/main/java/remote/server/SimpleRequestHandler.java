package remote.server;

import remote.dto.RpcRequest;
import remote.dto.RpcResponse;
import util.serialize.SerializeFactory;

import java.io.*;
import java.lang.reflect.Method;

/**
 * Created by JackJ on 2021/1/16.
 */
public class SimpleRequestHandler implements RequestHandler {


    @Override
    public void handler(InputStream inputStream, OutputStream outputStream) throws IOException {
        RpcResponse response = new RpcResponse();
        try {
            RpcRequest request = readData(inputStream);

            Class<?> cls = Class.forName(request.getClassName());
            Method method = checkoutIfExistMethod(request.getMethodName(), cls);
            Object obj = cls.newInstance();
            Object result = method.invoke(obj, request.getParams());

            response.setHasException(false);
            response.setResult(result);
        } catch (Exception e) {
            response.setHasException(true);
            response.setResult(e);
        }

        byte[] result = SerializeFactory.getSerializeUtil().serialize(response);
        outputStream.write(result);
    }

    private RpcRequest readData(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024 * 1024]; // TODO: 2021/2/21 这个应该变为可以进行配置的

        int length = inputStream.read(buffer); // TODO: 2021/2/21 这里我总感觉我从来没有搞清楚过 我就不知道我 读没读完或者 应该怎么读 有没有可能我读一半就读不到了？

        byte[] result = new byte[length];
        System.arraycopy(buffer, 0, result, 0, length);

        return SerializeFactory.getSerializeUtil().deserialize(RpcRequest.class, result);
    }

    private Method checkoutIfExistMethod(String methodName, Class cls) throws Exception {
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
