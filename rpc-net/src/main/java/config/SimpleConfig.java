package config;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by JackJ on 2021/1/17.
 */
public class SimpleConfig implements Config {

    private static final Map<String, String> config = new ConcurrentHashMap<>();

    private static boolean isInit = false;

    @Override
    public String get(String key) {
        return config.get(key);
    }

    @Override
    public void put(String key, String value) {
        config.put(key, value);
    }

    @Override
    public void init(Properties properties) {
        if(!isInit) {
            synchronized (this) {
                if (!isInit) {
                    Map<String, String> exMap = (Map) properties;
                    Set<Map.Entry<String, String>> entryIterator = exMap.entrySet();
                    for (Map.Entry<String, String> entry : entryIterator) {
                        config.put(entry.getKey(), entry.getValue());
                    }
                    isInit = true;
                }
            }
        }
    }
}
