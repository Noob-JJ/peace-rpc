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


    public String getNode(){
        // TODO: 2021/1/16 如果存在多个节点进行负载均衡策略  那就要注意一点 这个方法的调用一定是伴随着每一次服务的调用不然负载均衡就没有起效
        return "";
    }

}
