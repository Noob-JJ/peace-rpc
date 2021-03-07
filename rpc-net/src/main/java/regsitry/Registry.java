package regsitry;

import entity.Provider;
import io.netty.resolver.InetSocketAddressResolver;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Created by JackJ on 2021/1/16.
 */
public interface Registry {

    void registry(String serviceName, String value);

    List<String> getNode(String service);

}
