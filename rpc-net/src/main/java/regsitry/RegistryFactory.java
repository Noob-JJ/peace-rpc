package regsitry;

/**
 * Created by JackJ on 2021/1/16.
 */
public class RegistryFactory {


    public static Registry getRegistry(){
        // TODO: 2021/1/16 根据配置文件返回注册中心实例
        // TODO: 2021/1/16 这里可以使用工厂方法 哈哈
        return new Zookeeper();
    }
}
