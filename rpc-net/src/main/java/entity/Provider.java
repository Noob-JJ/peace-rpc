package entity;

import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import config.SimpleConfig;
import exception.RpcException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by JackJ on 2021/1/16.
 */
public class Provider {

    private String implClassName;

    private List<String> hostAndPort;

    private Provider(String implClassName, List<String> hostAndPort) {
        this.implClassName = implClassName;
        this.hostAndPort = hostAndPort;
    }

    public static Provider of(List<String> value){
        if (value == null || value.size() == 0) {
            throw new RpcException("[远程调用失败]:没有发现被注册的远程服务");
        }

        String implClassName = value.get(0).split(",")[0];

        List<String> hostAndPort = value.stream()
                .map(s -> s.split(",")[1])
                .collect(Collectors.toList());

        return new Provider(implClassName, hostAndPort);
    }


    public Node peekNode() {
         if (hostAndPort.size() == 1) {
            String[] infos = hostAndPort.get(0).split(":");
            return new Node(infos[0], infos[1]);
        } else if (SimpleConfig.INSTANCE.get("rpc.load.balance") == null) {
            String[] infos = hostAndPort.get(0).split(":");
            return new Node(infos[0], infos[1]);
        } else {
            String[] infos = hostAndPort.get(0).split(":");
            return new Node(infos[0], infos[1]);
        }

    }

    public String getImplClassName() {
        return implClassName;
    }

    public static class Node {

        private String host;

        private int port;

        private Node(String host, String port) {
            this.host = host;
            this.port = Integer.parseInt(port);
        }

        public int getPort() {
            return port;
        }

        public String getHost() {
            return host;
        }
    }

}
