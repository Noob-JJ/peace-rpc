package start;

import com.google.common.base.Strings;
import config.Config;
import config.ConfigTem;
import config.SimpleConfig;
import exception.RpcException;
import remote.server.NettyServer;
import remote.server.RpcServer;
import remote.handler.SimpleRequestHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proxy.InvocationHandlerFactory;
import regsitry.Registry;
import regsitry.RegistryFactory;
import remote.server.Server;
import util.FileUtils;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

/**
 * Created by JackJ on 2021/1/16.
 */
public class Rpc {

    private String version;

    private String group;


    public Rpc version(String version) {
        SimpleConfig.INSTANCE.put("version", version);
        this.version = version;
        return this;
    }

    public Rpc group(String group){
        SimpleConfig.INSTANCE.put("group", group);
        this.group = group;
        return this;
    }

    public void start() {
        RegistryFactory.getRegistry().clearAllHook();
        Server nettyServer = new NettyServer(Integer.parseInt(SimpleConfig.INSTANCE.get("provider.port")), new SimpleRequestHandler());
        nettyServer.start();
    }

    public Rpc registry(Class<?> registryService) {

        if (Objects.isNull(registryService)) {
            throw new RpcException("注册的服务不能为null");
        }

        Registry registry = RegistryFactory.getRegistry();

        String serviceName = genServiceName(registryService);
        String value = null;

        try {
            value = registryService.getCanonicalName() + "," + InetAddress.getLocalHost().getHostAddress() + ":" + Integer.parseInt(SimpleConfig.INSTANCE.get("provider.port"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        registry.registry(serviceName, value);


        return this;
    }

    private String genServiceName(Class<?> service){
        String className = service.getInterfaces()[0].getCanonicalName();

        return className + this.group + this.version;
    }


    public <T> T getBean(Class<T> tClass) {
        return (T) InvocationHandlerFactory.getRpcProxy(tClass);
    }

}
