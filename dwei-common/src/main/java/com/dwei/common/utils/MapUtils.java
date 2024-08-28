package com.dwei.common.utils;

import lombok.NonNull;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Map工具类
 *
 * @author hww
 */
public abstract class MapUtils {

    public static <K, V> Map<K, V> of() {
        return new HashMap<>();
    }

    public static <K, V> Map<K, V> ofSafe() {
        return new ConcurrentHashMap<>();
    }

    public static <K, V> Map<K, V> ofType(Map<K, V> map) {
        if (map instanceof ConcurrentHashMap) return ofSafe();
        if (map instanceof Hashtable<K, V>) return new Hashtable<>();
        return of();
    }

    public static <K, V> boolean isEmpty(Map<K, V> map) {
        if (Objects.isNull(map)) return true;
        return map.isEmpty();
    }

    public static <K, V> boolean isNotEmpty(Map<K, V> map) {
        return !isEmpty(map);
    }

    public static <K, V, T> Map<K, T> map(Map<K, V> map, @NonNull Function<V, T> valueMapper) {
        Map<K, T> newMap = of();
        map.forEach((k, v) -> newMap.put(k, valueMapper.apply(v)));
        return newMap;
    }

    public static <K, V, K2, V2> Map<K2, V2> map(Map<K, V> map,
                                                 @NonNull Function<K, K2> keyMapper,
                                                 @NonNull Function<V, V2> valueMapper) {
        Map<K2, V2> newMap = of();
        map.forEach((k, v) -> newMap.put(keyMapper.apply(k), valueMapper.apply(v)));
        return newMap;
    }

    public static <K, V> Map<K, V> clone(Map<K, V> source) {
        if (Objects.isNull(source)) return null;
        return new HashMap<>(source);
    }

    public static <K, V> Map<K, V> filterK(Map<K, V> map, Function<K, Boolean> filter) {
        return filter(map, filter, null);
    }

    public static <K, V> Map<K, V> filterV(Map<K, V> map, Function<V, Boolean> filter) {
        return filter(map, null, filter);
    }

    public static <K, V> Map<K, V> filter(Map<K, V> map, Function<K, Boolean> keyFilter, Function<V, Boolean> valueFilter) {
        if (Objects.isNull(map)) return null;
        if (map.isEmpty()) return null;
        Map<K, V> newMap = of();
        map.forEach((k, v) -> {
            if (Objects.nonNull(keyFilter) && (!keyFilter.apply(k))) return;
            if (Objects.nonNull(valueFilter) && (!valueFilter.apply(v))) return;
            newMap.put(k, v);
        });
        return newMap;
    }

}
