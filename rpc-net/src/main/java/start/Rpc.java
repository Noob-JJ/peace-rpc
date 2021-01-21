package start;

import config.Config;
import config.ConfigTem;
import config.SimpleConfig;
import net.server.RpcServer;
import net.server.SimpleRequestHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proxy.InvocationHandlerFactory;
import regsitry.Registry;
import regsitry.RegistryFactory;
import util.FileUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
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
        URL url = FileUtils.loadFile("META_INF/rpc.properties");
        Properties properties = new Properties();
        properties.load(url.openStream());
        SimpleConfig.INSTANCE.init(properties);
        changeConfig();
        LOGGER.info("start registry service");
        registry();
        LOGGER.info("start subscribe service");
        sub();
        LOGGER.info("start server");
        RpcServer rpcServer = new RpcServer(Integer.parseInt(config.getProviderPort()), new SimpleRequestHandler());
        Thread thread = new Thread(() -> {
            rpcServer.start();
            // TODO: 2021/1/17 怎么替换 
        });
        thread.start();
    }

    private void sub() throws Exception {
        Registry registry = RegistryFactory.getRegistry();
        String[] classNames = config.getSubcribeClass().split(",");
        registry.subscribe(classNames);
    }

    private void registry() throws Exception {
        Registry registry = RegistryFactory.getRegistry();
        String[] classNames = config.getProviderClass().split(",");
        String name = config.getName();
        String port = config.getProviderPort();
        String ip = config.getProviderIp();
        for (String className : classNames) {
            if(!className.equals("")) {
                registry.registry(name, className, ip, port);
            }
        }
    }

    private void changeConfig() throws UnknownHostException {
        Config simpleConfig = SimpleConfig.INSTANCE;
        InetAddress addr = InetAddress.getLocalHost();
        String registry = simpleConfig.get("rpc.registry");
        String registryHost = simpleConfig.get("rpc.registry.host");
        String providerIp = addr.getHostAddress();
        String providerPort = simpleConfig.get("rpc.provider.port");
        String providerClass = simpleConfig.get("rpc.provider.class");
        String subcribeClass = simpleConfig.get("rpc.subcribe.class");
        String name = simpleConfig.get("rpc.name");
        config = new ConfigTem(registry, registryHost, providerIp, providerPort, providerClass, subcribeClass, name);
    }

    public static Rpc getInstace() throws Exception {
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
