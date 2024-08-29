package com.dwei.common.utils;

import java.util.Collection;

/**
 * Collection集合工具类
 *
 * @author hww
 */
public abstract class CollectionUtils {

    public static <T> boolean isEmpty(Collection<T> collection) {
        return org.apache.commons.collections4.CollectionUtils.isEmpty(collection);
    }

    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return !isEmpty(collection);
    }

    public static <T> T first(Collection<T> collection) {
        return first(collection, null);
    }

    public static <T> T first(Collection<T> collection, T defaultElement) {
        if (isEmpty(collection)) return defaultElement;
        return collection.iterator().next();
    }

    public static <T> T last(Collection<T> collection) {
        return last(collection, null);
    }

    public static <T> T last(Collection<T> collection, T defaultElement) {
        if (isEmpty(collection)) return defaultElement;
        return collection.stream().toList().get(collection.size() - 1);
    }

    public static <T> T any(Collection<T> collection) {
        return any(collection, null);
    }

    public static <T> T any(Collection<T> collection, T defaultElement) {
        if (isEmpty(collection)) return defaultElement;
        var s = collection.stream().findAny();
        return s.orElse(defaultElement);
    }

}
