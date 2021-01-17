package config;

/**
 * Created by JackJ on 2021/1/17.
 */
public final class ConfigTem {

    private final String registry;

    private final String registryHost;

    private final String providerIp;

    private final String providerPort;

    private final String providerClass;

    private final String subcribeClass;

    private final String name;


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
