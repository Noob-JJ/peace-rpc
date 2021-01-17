package regsitry;


import config.SimpleConfig;
import util.FileUtils;

/**
 * Created by JackJ on 2021/1/16.
 */
public class RegistryFactory {


    public static Registry getRegistry(){
        String registry = SimpleConfig.INSTANCE.get("registry");
        if (registry.toLowerCase().equals("zookeeper")) {
            return Zookeeper.INSTANCE;
        }
        return Zookeeper.INSTANCE;
    }
}
