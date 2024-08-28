package com.dwei.common.utils;

import java.util.Arrays;
import java.util.Collection;

/**
 * 断言工具类
 *
 * @author hww
 */
public abstract class Assert {

    private static final String DEFAULT_MESSAGE = "[Assertion failed]";

    public static void isTrue(boolean expression) {
        if (!expression) throw new IllegalArgumentException(DEFAULT_MESSAGE);
    }

    public static void isTrue(boolean expression, String errorMsgTemplate, Object... params) {
        if (!expression) throw new IllegalArgumentException(StringUtils.format(errorMsgTemplate, params));
    }

    public static void isFalse(boolean expression) {
        if (expression) throw new IllegalArgumentException(DEFAULT_MESSAGE);
    }

    public static void isFalse(boolean expression, String errorMsgTemplate, Object... params) {
        if (expression) throw new IllegalArgumentException(StringUtils.format(errorMsgTemplate, params));
    }

    public static void equals(Object p1, Object p2) {
        if (!ObjectUtils.equals(p1, p2)) throw new IllegalArgumentException(DEFAULT_MESSAGE);
    }

    public static void equals(Object p1, Object p2, String errorMsgTemplate, Object... params) {
        if (!ObjectUtils.equals(p1, p2)) throw new IllegalArgumentException(StringUtils.format(errorMsgTemplate, params));
    }

    public static void notEquals(Object p1, Object p2) {
        if (ObjectUtils.equals(p1, p2)) throw new IllegalArgumentException(DEFAULT_MESSAGE);
    }

    public static void notEquals(Object p1, Object p2, String errorMsgTemplate, Object... params) {
        if (ObjectUtils.equals(p1, p2)) throw new IllegalArgumentException(StringUtils.format(errorMsgTemplate, params));
    }

    public static void isNull(Object object) {
        if (ObjectUtils.nonNull(object)) throw new IllegalArgumentException(DEFAULT_MESSAGE);
    }

    public static void isNull(Object object, String errorMsgTemplate, Object... params) {
        if (ObjectUtils.nonNull(object)) throw new IllegalArgumentException(StringUtils.format(errorMsgTemplate, params));
    }

    public static void nonNull(Object object) {
        if (ObjectUtils.isNull(object)) throw new IllegalArgumentException(DEFAULT_MESSAGE);
    }

    public static void nonNull(Object object, String errorMsgTemplate, Object... params) {
        if (ObjectUtils.isNull(object)) throw new IllegalArgumentException(StringUtils.format(errorMsgTemplate, params));
    }

    public static void isNotEmpty(Collection<?> collection) {
        if (ObjectUtils.isNull(collection)) throw new IllegalArgumentException(DEFAULT_MESSAGE);
    }

    public static void isNotEmpty(Collection<?> collection, String errorMsgTemplate, Object... params) {
        if (ObjectUtils.isNull(collection)) throw new IllegalArgumentException(StringUtils.format(errorMsgTemplate, params));
    }

    public static void isEmpty(Collection<?> collection) {
        if (ObjectUtils.nonNull(collection)) throw new IllegalArgumentException(DEFAULT_MESSAGE);
    }

    public static void isEmpty(Collection<?> collection, String errorMsgTemplate, Object... params) {
        if (ObjectUtils.nonNull(collection)) throw new IllegalArgumentException(StringUtils.format(errorMsgTemplate, params));
    }

    public static void isStrBlank(String obj) {
        if (ObjectUtils.nonNull(obj)) throw new IllegalArgumentException(DEFAULT_MESSAGE);
    }

    public static void isStrBlank(String obj, String errorMsgTemplate, Object... params) {
        if (ObjectUtils.nonNull(obj)) throw new IllegalArgumentException(StringUtils.format(errorMsgTemplate, params));
    }

    public static void isStrNotBlank(String obj) {
        if (ObjectUtils.isNull(obj)) throw new IllegalArgumentException(DEFAULT_MESSAGE);
    }

    public static void isStrNotBlank(String[] obj) {
        Arrays.stream(obj).forEach(Assert::isStrNotBlank);
    }

    public static void isStrNotBlank(String obj, String errorMsgTemplate, Object... params) {
        if (ObjectUtils.isNull(obj)) throw new IllegalArgumentException(StringUtils.format(errorMsgTemplate, params));
    }

    public static void isStrNotBlank(String[] obj, String errorMsgTemplate, Object... params) {
        Arrays.stream(obj).forEach(t -> isStrNotBlank(t, errorMsgTemplate, params));
    }

}
