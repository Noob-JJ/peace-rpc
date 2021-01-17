package regsitry;


import config.SimpleConfig;
import start.Rpc;
import util.FileUtils;

/**
 * Created by JackJ on 2021/1/16.
 */
public class RegistryFactory {


    public static Registry getRegistry() throws Exception {
        String registry = Rpc.getInstace().getConfig().getRegistry();
        if (registry.toLowerCase().equals("zookeeper")) {
            return Zookeeper.INSTANCE;
        }
        return Zookeeper.INSTANCE;
    }
}
