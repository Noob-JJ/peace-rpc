package regsitry;

import entity.Provider;
import org.apache.zookeeper.CreateMode;

/**
 * Created by JackJ on 2021/1/16.
 */
public interface Registry {

    void registry(String serviceName, String className, String host, String port) throws Exception;

    Provider getProvider(String className) throws Exception;

    void subscribe(String[] className) throws Exception;

}
