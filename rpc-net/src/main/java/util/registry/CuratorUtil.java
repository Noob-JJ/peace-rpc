package util.registry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;

import java.util.List;

public class CuratorUtil {


    public static void createChildNode(CuratorFramework zkClient, String path) {
        try {

            zkClient.create().creatingParentsIfNeeded().
                    withMode(CreateMode.EPHEMERAL).forPath(path);

        } catch (Exception e) {
            throw new RuntimeException("[注册服务失败]：zookeeper添加节点出错");
        }
    }

    public static List<String> getNodeValue(CuratorFramework zkClient, String path) {
        List<String> childrenNodes = null;
        try {

            childrenNodes = zkClient.getChildren().forPath(path);

        } catch (Exception e) {
            throw new RuntimeException("[获取服务地址失败]：zookeeper获取节点数据出错");
        }

        return childrenNodes;
    }

    public static void listenNode(CuratorFramework zkClient, String path, PathChildrenCacheListener pathChildrenCacheListener) {
        try {
            PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, path, true);
            pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
            pathChildrenCache.start();
        } catch (Exception e) {
            throw new RuntimeException("[监听服务错误]:zookeeper添加监听器出错");
        }
    }
}
