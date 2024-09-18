package com.dwei.common.utils;

import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 对象工具类
 * 主要是对jdk的Objects的补强
 *
 * @author hww
 */
public abstract class ObjectUtils {

    public static <T> T self(T obj) {
        return obj;
    }

    public static boolean equals(Object a, Object b) {
        return Objects.equals(a, b);
    }

    public static boolean isNotEquals(Object a, Object b) {
        return !equals(a, b);
    }

    public static boolean nonNull(Object obj) {
        return !isNull(obj);
    }

    public static boolean isNull(Object obj) {
        if (obj == null) return true;

        if (obj instanceof String) return StringUtils.isBlank((String) obj);
        if (obj instanceof CharSequence) return StringUtils.isBlank((CharSequence) obj);
        if (obj instanceof Collection) return CollectionUtils.isEmpty((Collection<?>) obj);
        if (obj instanceof Map) return Maps.isEmpty((Map<?, ?>) obj);
        if (obj.getClass().isArray()) return Array.getLength(obj) == 0;
        if (obj instanceof Optional<?> optional) return optional.map(ObjectUtils::isNull).orElse(true);

        // else
        return false;
    }

}
