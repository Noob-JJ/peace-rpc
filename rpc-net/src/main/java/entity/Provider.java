package entity;

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
        // TODO: 2021/1/17 负载均衡的策略也是通过配置文件来的
        return new Node("", "", "");
    }
    
    public static class Node{
        private String serviceName;
        
        private String host;
        
        private int port;

        public Node(String serviceName, String host, String port) {
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
