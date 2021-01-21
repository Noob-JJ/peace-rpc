package net.server;

import net.entity.RpcRequest;
import net.entity.RpcResponse;
import start.Rpc;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by JackJ on 2021/1/16.
 */
public class SimpleRequestHandler implements RequestHandler {


    @Override
    public void handler(InputStream inputStream, OutputStream outputStream) throws IOException {
        RpcResponse response = new RpcResponse();
        try {
            RpcRequest request = (RpcRequest) new ObjectInputStream(inputStream).readObject();
            Class cls = Class.forName(request.getClassName());
            Method method = checkoutIfExistMethod(request.getMethodName(), cls);
            Object obj = cls.newInstance();
            Object result = method.invoke(obj, request.getParams());
            response.setHasException(false);
            response.setResult(result);
        } catch (Exception e) {
            response.setHasException(true);
            response.setResult(e);
        }
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(response);
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

    private boolean checkoutParamsCorrect(Method method, Object[] params) {
        if (params == null || params.length == 0) {
            return true;
        }
        if (!(params.length == method.getParameterCount())) {
            return false;
        }
        else{
            Class[] clss = method.getParameterTypes();
            for(int i = 0;i < params.length;i++) {
                if (params[i].getClass() != clss[i]) {
                    return false;
                }
            }
        }
        return true;
    }

}
