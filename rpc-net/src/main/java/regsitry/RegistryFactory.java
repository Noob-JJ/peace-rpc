package regsitry;


import config.SimpleConfig;
import start.Rpc;
import util.FileUtils;

/**
 * Created by JackJ on 2021/1/16.
 */
public class RegistryFactory {


    public static Registry getRegistry() {
        return Zookeeper.INSTANCE;
    }
}
