package config;

import util.FileUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by JackJ on 2021/1/17.
 */
public enum SimpleConfig implements Config {
    INSTANCE;

    private static final Map<String, String> config = new ConcurrentHashMap<>();

    static {
        try {
            URL url = FileUtils.loadFile("META-INF/rpc.properties");
            Properties properties = new Properties();
            properties.load(url.openStream());
            properties.forEach((key, value) -> config.put(key.toString(), value.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String get(String key) {
        return config.get(key);
    }

    @Override
    public void put(String key, String value) {
        config.put(key, value);
    }


}
