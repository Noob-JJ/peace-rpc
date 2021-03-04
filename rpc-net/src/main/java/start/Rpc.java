package start;

import com.google.common.base.Strings;
import config.Config;
import config.ConfigTem;
import config.SimpleConfig;
import remote.server.NettyServer;
import remote.server.RpcServer;
import remote.handler.SimpleRequestHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proxy.InvocationHandlerFactory;
import regsitry.Registry;
import regsitry.RegistryFactory;
import util.FileUtils;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Properties;

/**
 * Created by JackJ on 2021/1/16.
 */
public class Rpc {

    private static final Logger LOGGER = LoggerFactory.getLogger(Rpc.class);

    private static ConfigTem config;

    private static Rpc instace;

    private Rpc(){

    }

    private void init() throws Exception {
        LOGGER.info("rpc init start");
        LOGGER.info("read config file");
        URL url = FileUtils.loadFile("META-INF/rpc.properties");
        Properties properties = new Properties();
        properties.load(url.openStream());
        SimpleConfig.INSTANCE.init(properties);
        changeConfig();
        LOGGER.info("start registry service");
        registry();
        LOGGER.info("start subscribe service");
        sub();
        LOGGER.info("start server");
        NettyServer rpcServer = new NettyServer(Integer.parseInt(config.getProviderPort()), new SimpleRequestHandler());
        Thread thread = new Thread(rpcServer::start);
        thread.start();
    }

    private void registry() {
        String providerClasses = config.getProviderClass();

        if (Strings.isNullOrEmpty(providerClasses)) {
            return ;
        }

        Registry registry = RegistryFactory.getRegistry();

        String[] classNames = providerClasses.split(",");
        String name = config.getName();
        String port = config.getProviderPort();
        String ip = config.getProviderIp();

        Arrays.stream(classNames).filter(className -> className != null && className.length() != 0).forEach(className -> {
            try {
                registry.registry(name, className, ip, port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void sub() throws Exception {
        Registry registry = RegistryFactory.getRegistry();

        String[] classNames = config.getSubscribeClass().split(",");

        registry.subscribe(classNames);
    }

    private void changeConfig() throws UnknownHostException {
        Config simpleConfig = SimpleConfig.INSTANCE;
        InetAddress address = InetAddress.getLocalHost();
        String registry = simpleConfig.get("rpc.registry");
        String registryHost = simpleConfig.get("rpc.registry.host");
        String providerIp = address.getHostAddress();
        String providerPort = simpleConfig.get("rpc.provider.port");
        String providerClass = simpleConfig.get("rpc.provider.class");
        String subscribeClass = simpleConfig.get("rpc.subscribe.class");
        String name = simpleConfig.get("rpc.name");
        config = new ConfigTem().setName(name)
                .setProviderClass(providerClass)
                .setProviderIp(providerIp)
                .setProviderPort(providerPort)
                .setRegistry(registry)
                .setRegistryHost(registryHost)
                .setSubscribeClass(subscribeClass);
    }

    public static Rpc getInstance() throws Exception {
        if (instace == null) {
            synchronized (Rpc.class) {
                if (instace == null) {
                    instace = new Rpc();
                    instace.init();
                }
            }
        }
        return instace;
    }

    public <T> T getBean(Class<T> tClass) {
        return (T) InvocationHandlerFactory.getRpcProxy(tClass);
    }

    public static ConfigTem getConfig() {
        return config;
    }
}
