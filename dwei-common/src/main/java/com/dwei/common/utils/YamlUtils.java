package com.dwei.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 配置加载工具类
 *
 * @author hww
 */
@Slf4j
public abstract class YamlUtils {

    public static Map<String, ?> load(String path) {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            Yaml yaml = new Yaml();
            return yaml.load(inputStream);
        } catch (Exception e) {
            log.debug("配置文件读取异常", e);
        }
        return null;
    }

    public static String get(Map<String, ?> map, String value) {
        String[] split = value.split("\\.");
        Map<String, ?> result = map;
        for (int i = 0; i < split.length; i++) {
            String key = split[i];
            if (i == split.length - 1) {
                return getAsString(result, key);
            }
            result = getAsMap(result, key);
        }
        return null;
    }

    public static Map<String, ?> getAsMap(Map<String, ?> map, String key) {
        //noinspection unchecked
        return (Map<String, ?>) map.get(key);
    }

    public static <T> Map<String, T> getAsMapT(Map<String, ?> map, String key) {
        //noinspection unchecked
        return (Map<String, T>) map.get(key);
    }

    public static String getAsString(Map<String, ?> map, String key) {
        return (String) map.get(key);
    }

    public static List<Map<String, String>> getAsList(Map<String, ?> map, String key) {
        //noinspection unchecked
        return (List<Map<String, String>>) map.get(key);
    }

}
