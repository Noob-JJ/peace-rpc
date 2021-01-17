package proxy;

import java.lang.reflect.Proxy;

/**
 * Created by JackJ on 2021/1/17.
 */
public class InvocationHandlerFactory {


    public static Object getRpcProxy(Class target){
        Class<?> cls[] = new Class[1];
        cls[0] = target;
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                cls, new RpcInvocationHandler(target));
    }

}
