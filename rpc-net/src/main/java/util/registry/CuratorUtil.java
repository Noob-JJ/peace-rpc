package util.registry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CuratorUtil {


    public static void createChildNode(CuratorFramework zkClient, String path, String value) {
        try {

            zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path, value.getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            throw new RuntimeException("[注册服务失败]：zookeeper添加节点出错");
        }
    }

    public static List<String> getNodeValue(CuratorFramework zkClient, String path) {
        List<String> result;

        try {

            List<String> childrenNodes = zkClient.getChildren().forPath(path);

            result = childrenNodes.stream()
                    .map(childrenNode -> {
                        String address = null;
                        try {
                            address = new String(zkClient.getData().forPath(path + "/" + childrenNode));
                        } catch (Exception e) {
                            throw new RuntimeException("[获取服务地址失败]：zookeeper获取节点数据出错");
                        }
                        return address;
                    }).collect(Collectors.toList());


        } catch (Exception e) {
            throw new RuntimeException("[获取服务地址失败]：zookeeper获取节点数据出错");
        }

        return result;
    }

    public static void listenNode(CuratorFramework zkClient, String path) {

    }
}
