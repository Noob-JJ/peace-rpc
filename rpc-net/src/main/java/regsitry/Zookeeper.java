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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Created by JackJ on 2021/1/16.
 */

// TODO: 2021/3/7 添加程序关闭hook取消registry的注册
enum Zookeeper implements Registry {
    INSTANCE;

    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRY = 3;
    private static final CuratorFramework zkClient;
    private static final Map<String, List<String>> serviceLocalBackup = new ConcurrentHashMap<>();
    private static final List<String> providerServicePath = new CopyOnWriteArrayList<>();

    static {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRY);
        zkClient = CuratorFrameworkFactory.builder().connectString(SimpleConfig.INSTANCE.get("rpc.registry.host")).retryPolicy(retryPolicy).build();
        zkClient.start();
    }

    public void clearAllHook(){
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
            providerServicePath.forEach(path -> {
                try {
                    System.out.println(path);
                    zkClient.delete().forPath(path);
                    System.out.println("删除节点成功");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            })
        ));

    }

    @Override
    public void registry(String serviceName, String value) {
        String path = genRegistryPath(serviceName, value);
        CuratorUtil.createChildNode(zkClient, path);
        providerServicePath.add(path);
    }


    @Override
    public List<String> getNode(String serviceName) {
        String path = genFindPath(serviceName);

        List<String> local = serviceLocalBackup.get(serviceName);
        if (local == null || local.isEmpty()) {
            CuratorUtil.listenNode(zkClient, path, (curatorFramework, pathChildrenCacheEvent) -> {
                List<String> serviceAddresses = curatorFramework.getChildren().forPath(path);
                serviceLocalBackup.put(serviceName, serviceAddresses);
            });

            List<String> result = CuratorUtil.getNodeValue(zkClient, path);
            serviceLocalBackup.put(serviceName, result);

            return result;
        }

        return local;
    }

    private String genFindPath(String serviceName) {
        return RpcConstant.ROOT_NODE_REGISTRY + "/" + serviceName;
    }

    private String genRegistryPath(String serviceName, String value) {

        return genFindPath(serviceName) + "/" + value;
    }


}
