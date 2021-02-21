package regsitry;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by JackJ on 2021/1/16.
 */
enum Zookeeper implements Registry {
    INSTANCE;

    private static final Map<String, Provider> provider = new ConcurrentHashMap<>();

    private static final int BASE_SLEEP_TIME = 1000;

    private static final int MAX_RETRY = 3;

    private static CuratorFramework zkClient;

    static {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRY);
        zkClient = CuratorFrameworkFactory.builder().connectString(SimpleConfig.INSTANCE.get("rpc.registry.host")).retryPolicy(retryPolicy).build(); //how
        zkClient.start();
    }

    @Override
    public void registry(String serviceName, String className, String host, String port) throws Exception {
        String[] strings = className.split(":");
        String path = "/rpc/" + strings[0] + "/provider/node";

        zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path, (serviceName + ":" + host + ":" + port).getBytes());

        zkClient.setData().forPath("/rpc/" + strings[0], strings[1].getBytes());
    }


    @Override
    public Provider getProvider(String className) throws Exception {
        return getNode(className);
    }


    @Override
    public void subscribe(String[] classNames) {
        Arrays.stream(classNames).forEach(className -> {
            try {
                addListener(className);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void addListener(String className) throws Exception {
        String classNamePath = "/rpc/" + className + "/provider";
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, classNamePath, true);
        PathChildrenCacheListener pathChildrenCacheListener = (curatorFramework, pathChildrenCacheEvent) -> {
            switch (pathChildrenCacheEvent.getType()) {
                case CHILD_ADDED:

                case CHILD_UPDATED:

                case CHILD_REMOVED:
                    String implClassName = new String(zkClient.getData().forPath("/rpc/" + className));
                    List<String> ipAndPorts = new ArrayList<>();
                    List<String> childNodes = zkClient.getChildren().forPath(classNamePath);
                    for (String childNode : childNodes) {
                        ipAndPorts.add(new String(zkClient.getData().forPath(classNamePath + "/" + childNode)));
                    }
                    provider.put(className, new Provider(implClassName, className, ipAndPorts));
                    break;
                case CONNECTION_LOST:
                    break;
                case CONNECTION_RECONNECTED:
                    break;
                case CONNECTION_SUSPENDED:
                    break;
                case INITIALIZED:
                    break;
                default:
                    break;
            }
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        pathChildrenCache.start();
    }

    private Provider getNode(String className) throws Exception {
        if (provider.get(className) == null) {
            String path = "/rpc/" + className + "/provider";
            String implClassName = new String(zkClient.getData().forPath("/rpc/" + className));
            List<String> ipAndPorts = new ArrayList<>();
            List<String> childNodes = zkClient.getChildren().forPath(path);
            for (String childNode : childNodes) {
                ipAndPorts.add(new String(zkClient.getData().forPath(path + "/" + childNode)));
            }
            return new Provider(implClassName, className, ipAndPorts);
        }
        return provider.get(className);
    }

    public static void main(String[] args) throws Exception {

    }
}
