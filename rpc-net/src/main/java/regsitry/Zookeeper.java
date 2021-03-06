package regsitry;

import common.RpcConstant;
import config.SimpleConfig;
import entity.Provider;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import util.registry.CuratorUtil;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by JackJ on 2021/1/16.
 */
enum Zookeeper implements Registry {
    INSTANCE;

    private static final int BASE_SLEEP_TIME = 1000;

    private static final int MAX_RETRY = 3;

    private static CuratorFramework zkClient;

    static {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRY);
        zkClient = CuratorFrameworkFactory.builder().connectString(SimpleConfig.INSTANCE.get("rpc.registry.host")).retryPolicy(retryPolicy).build(); //how
        zkClient.start();
    }

    @Override
    public void registry(String serviceName, InetSocketAddress address) {
        String path = RpcConstant.ROOT_NODE_REGISTRY + "/" + serviceName + "/node";

        CuratorUtil.createChildNode(zkClient, path, address.toString());

    }


    @Override
    public List<String> getNode(String serviceName) {
        return CuratorUtil.getNodeValue(zkClient, serviceName);
    }



}
