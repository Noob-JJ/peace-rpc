package config;

/**
 * Created by JackJ on 2021/1/17.
 */
public final class ConfigTem {

    private final String registry; //注册中心 类型

    private final String registryHost; //注册中心地址

    private final String providerIp; //提供者的ip

    private final String providerPort; //提供者端口号

    private final String providerClass;//提供类

    private final String subcribeClass;//订阅类

    private final String name;//服务名


    public ConfigTem(String registry, String registryHost, String providerIp, String providerPort, String providerClass, String subcribeClass, String name) {
        this.name = name == null ? "defaultName" : name;
        this.registry = registry == null ? "zookeeper" : registry;
        this.registryHost = registryHost == null ? "" : registryHost;
        this.providerIp = providerIp == null ? "" : providerIp;
        this.providerPort = providerPort == null ? "" : "333";
        this.providerClass = providerClass == null ? "" : providerClass;
        this.subcribeClass = subcribeClass == null ? "" : subcribeClass;
    }

    public String getRegistry() {
        return registry;
    }

    public String getRegistryHost() {
        return registryHost;
    }

    public String getProviderPort() {
        return providerPort;
    }

    public String getProviderClass() {
        return providerClass;
    }

    public String getSubcribeClass() {
        return subcribeClass;
    }

    public String getProviderIp() {
        return providerIp;
    }

    public String getName() {
        return name;
    }
}
