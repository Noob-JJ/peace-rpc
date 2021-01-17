package start;

import config.Config;
import config.ConfigTem;
import config.SimpleConfig;
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

    public static ConfigTem config;

    private static Rpc instace;

    private Rpc(){

    }

    private void init() throws Exception {
        URL url = FileUtils.loadFile("META_INF/rpc.properties");
        Properties properties = new Properties();
        properties.load(url.openStream());
        SimpleConfig.INSTANCE.init(properties);
        changeConfig();
        registry();
        sub();
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
            registry.registry(name,className, ip, port);
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
            // TODO: 2021/1/17 这里为什么会报错 单例模式不就是这么实现的吗
            synchronized ("lock") {
                if (instace == null) {
                    instace = new Rpc();
                    instace.init();
                }
            }
        }
        return instace;
    }

    // TODO: 2021/1/17 这里也看看json转换那边怎么实现的
    public <T> T getBean(Class<T> tClass) {
        return (T) InvocationHandlerFactory.getRpcProxy(tClass);
    }

    public static ConfigTem getConfig() {
        return config;
    }
}
