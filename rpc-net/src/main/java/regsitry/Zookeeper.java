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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by JackJ on 2021/1/16.
 */
enum Zookeeper implements Registry {
    INSTANCE;

    // TODO: 2021/1/17 这里需要优化一下， 我觉得 应该用provider来当value 因为从抽象来看一个className因该用一个provider来表示

    private static Map<String, List<String>> provider = new ConcurrentHashMap<>();

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
        String path = "/rpc/" + className + "/provider/node";
        createNode(CreateMode.EPHEMERAL_SEQUENTIAL, path, serviceName + ":" + host + ":" + port);
    }


    @Override
    public Provider getProvider(String className) throws Exception {
        List<String> hostAndPorts = getNode(className);
        return new Provider(className, hostAndPorts);
    }


    @Override
    public void subscribe(String[] classNames) throws Exception {
        for (String className : classNames) {
            addListener(className);
        }
    }

    private void createNode(CreateMode mode, String name, String value) throws Exception {
        zkClient.create().creatingParentsIfNeeded().withMode(mode).forPath(name, value.getBytes());
    }

    private void addListener(String className) throws Exception {
        String classNamePath = "/rpc/" + className + "/provider";
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, classNamePath, true);
        PathChildrenCacheListener pathChildrenCacheListener = (curatorFramework, pathChildrenCacheEvent) -> {
            switch (pathChildrenCacheEvent.getType()) {
                case CHILD_ADDED:

                case CHILD_UPDATED:

                case CHILD_REMOVED:
                    List<String> newNodes = curatorFramework.getChildren().forPath(classNamePath);
                    List<String> datas = new ArrayList<>();
                    for (String newNode : newNodes) {
                        datas.add(new String(curatorFramework.getData().forPath(classNamePath + "/" + newNode)));
                    }
                    provider.put(className, datas);
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

    private List<String> getNode(String className) throws Exception {
        if (provider.get(className) == null) {
            String path = "/rpc/" + className + "/provider";
            List<String> ipAndPorts = new ArrayList<>();
            List<String> childNodes = zkClient.getChildren().forPath(path);
            for (String childNode : childNodes) {
                System.out.println(childNode);
                ipAndPorts.add(new String(zkClient.getData().forPath(path + "/" + childNode)));
            }
            return ipAndPorts;
        }
        return provider.get(className);
    }

    public static void main(String[] args) throws Exception {
        Zookeeper zookeeper = INSTANCE;
        zookeeper.registry("test", "com.example.test", "127.0.0.1", "5005");
        for(String value : zookeeper.getNode("com.example.test")){
            System.out.println(value);
        }

    }
}
