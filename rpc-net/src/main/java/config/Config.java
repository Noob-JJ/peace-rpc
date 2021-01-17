package config;

import java.util.Properties;

/**
 * Created by JackJ on 2021/1/16.
 */
public interface Config {

    String get(String key);

    void put(String key, String value);

    void init(Properties properties);
}
