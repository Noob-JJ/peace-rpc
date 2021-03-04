package config;

/**
 * Created by JackJ on 2021/1/17.
 */
public final class ConfigTem {

    public String getRegistry() {
        return registry;
    }

    public ConfigTem setRegistry(String registry) {
        this.registry = registry;
        return this;
    }

    public String getRegistryHost() {
        return registryHost;
    }

    public ConfigTem setRegistryHost(String registryHost) {
        this.registryHost = registryHost;
        return this;
    }

    public String getProviderIp() {
        return providerIp;
    }

    public ConfigTem setProviderIp(String providerIp) {
        this.providerIp = providerIp;
        return this;
    }

    public String getProviderPort() {
        return providerPort;
    }

    public ConfigTem setProviderPort(String providerPort) {
        this.providerPort = providerPort;
        return this;
    }

    public String getProviderClass() {
        return providerClass;
    }

    public ConfigTem setProviderClass(String providerClass) {
        this.providerClass = providerClass;
        return this;
    }

    public String getSubscribeClass() {
        return subscribeClass;
    }

    public ConfigTem setSubscribeClass(String subscribeClass) {
        this.subscribeClass = subscribeClass;
        return this;
    }

    public String getName() {
        return name;
    }

    public ConfigTem setName(String name) {
        this.name = name;
        return this;
    }

    private String registry = "zookeeper"; //注册中心 类型

    private String registryHost; //注册中心地址

    private String providerIp; //提供者的ip

    private String providerPort = "333"; //提供者端口号

    private String providerClass;//提供类

    private String subscribeClass;//订阅类

    private String name = "defaultName";//服务名
}
