package regsitry;


import config.SimpleConfig;
import start.Rpc;
import util.FileUtils;

/**
 * Created by JackJ on 2021/1/16.
 */
public class RegistryFactory {


    public static Registry getRegistry() {
        String registry = Rpc.getConfig().getRegistry();
        if (registry.equalsIgnoreCase("zookeeper")) {
            return Zookeeper.INSTANCE;
        }
        return Zookeeper.INSTANCE;
    }
}
