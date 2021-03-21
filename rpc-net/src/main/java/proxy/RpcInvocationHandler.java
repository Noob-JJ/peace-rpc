package proxy;

import config.SimpleConfig;
import entity.Provider;
import exception.RpcException;
import remote.client.NettyRpcClient;
import remote.client.RpcClient;
import remote.dto.RpcRequest;
import remote.dto.RpcResponse;
import regsitry.RegistryFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

        List<String> values = RegistryFactory.getRegistry().getNode(serviceName);
        Provider provider = Provider.of(values);
        Provider.Node node = provider.peekNode();

        RpcRequest request = new RpcRequest(UUID.randomUUID().toString(), provider.getImplClassName(), method.getName(), args);

        RpcResponse response = NettyRpcClient.request(request, node.getHost(), node.getPort());

        if (Objects.isNull(response)) {
            throw new RpcException("[响应数据出错]:响应数据为null");
        }

        return response.get();
    }

    private String genServiceName(String className) {
        SimpleConfig config = SimpleConfig.INSTANCE;

        String group = config.get("group");
        String version = config.get("version");

        return className + group + version;

    }

}
