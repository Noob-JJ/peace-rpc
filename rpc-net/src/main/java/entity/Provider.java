package entity;

import config.SimpleConfig;

import java.util.List;

/**
 * Created by JackJ on 2021/1/16.
 */
public class Provider {

    private String className;

    private List<String> hostAndPort;

    public Provider(String className, List<String> hostAndPort) {
        this.className = className;
        this.hostAndPort = hostAndPort;
    }


    public Node getNode(){
        if (hostAndPort == null || hostAndPort.size() == 0) {
            throw new RuntimeException("not find provider for" + this.getClassName());
        } else if (hostAndPort.size() == 1) {
            String[] infos = hostAndPort.get(0).split(":");
            return new Node(infos[0], infos[1], infos[2]);
        } else if (SimpleConfig.INSTANCE.get("rpc.load.balance") == null) {
            return null;
        }
        else{
            return null;
        }

        // TODO: 2021/1/17 这里的负载均衡策略还需要进一步思考下
    }

    public String getClassName() {
        return className;
    }

    public static class Node{
        private String serviceName;
        
        private String host;
        
        private int port;

        private Node(String serviceName, String host, String port) {
            this.serviceName = serviceName;
            this.host = host;
            this.port = Integer.parseInt(port);
        }

        public int getPort() {
            return port;
        }

        public String getHost() {
            return host;
        }

        public String getServiceName() {
            return serviceName;
        }
    }

}
