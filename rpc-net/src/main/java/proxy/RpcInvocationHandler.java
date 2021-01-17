package proxy;

import entity.Provider;
import net.client.RpcClient;
import net.entity.RpcRequest;
import net.entity.RpcResponse;
import regsitry.RegistryFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by JackJ on 2021/1/17.
 */
public class RpcInvocationHandler implements InvocationHandler {

    private Object target;


     RpcInvocationHandler(Object target) {
        this.target = target;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Provider provider = RegistryFactory.getRegistry().getProvider(target.getClass().getName());
        Provider.Node node = provider.getNode();
        RpcRequest request = new RpcRequest(target.getClass().getName(), method.getName(), args);
        RpcResponse response = (RpcResponse)RpcClient.request(request, node.getHost(), node.getPort());
        assert response != null;
        return response.get();
    }
}