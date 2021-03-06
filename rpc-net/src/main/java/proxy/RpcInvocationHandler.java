package proxy;

import config.SimpleConfig;
import entity.Provider;
import remote.client.RpcClient;
import remote.dto.RpcRequest;
import remote.dto.RpcResponse;
import regsitry.RegistryFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by JackJ on 2021/1/17.
 */
public class RpcInvocationHandler implements InvocationHandler {

    private final Class<?> target;


     RpcInvocationHandler(Class<?> target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String serviceName = genServiceName(target.getCanonicalName());

        List<String> address = RegistryFactory.getRegistry().getNode(serviceName);
        Provider provider = new Provider(address);
        Provider.Node node = provider.peekNode();

        RpcRequest request = new RpcRequest(serviceName, method.getName(), args);

        RpcResponse response = RpcClient.request(request, node.getHost(), node.getPort());

        return response.get();
    }

    private String genServiceName(String className) {
        SimpleConfig config = SimpleConfig.INSTANCE;

        String group = config.get("group");
        String version = config.get("version");

        return className + group + version;

    }

}
