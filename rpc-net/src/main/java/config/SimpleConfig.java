package config;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by JackJ on 2021/1/17.
 */
public enum SimpleConfig implements Config {
    INSTANCE;

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
        if (!isInit) {
            synchronized (this) {
                if (!isInit) {
                    properties.forEach((key, value) -> config.put(key.toString(), value.toString()));
                    isInit = true;
                }
            }
        }
    }


}
