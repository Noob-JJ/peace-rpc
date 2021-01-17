package proxy;

import java.lang.reflect.Proxy;

/**
 * Created by JackJ on 2021/1/17.
 */
public class InvocationHandlerFactory {


    public static Object getRpcProxy(Object target){
        Class<?> cls[] = new Class[1];
        cls[0] = target.getClass();
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                cls, new RpcInvocationHandler(target));
    }

}
