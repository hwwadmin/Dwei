package com.dwei.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collection;

/**
 * 断言工具类
 *
 * @author hww
 */
@Slf4j
public abstract class Assert {

    private static final String DEFAULT_MESSAGE = "[Assertion failed]";

    public static void ex() {
        throw new IllegalArgumentException(DEFAULT_MESSAGE);
    }

    public static void ex(String errorMsgTemplate, Object... params) {
        throw new IllegalArgumentException(StringUtils.format(errorMsgTemplate, params));
    }

    public static void isTrue(boolean expression) {
        if (!expression) ex();
    }

    public static void isTrue(boolean expression, String errorMsgTemplate, Object... params) {
        if (!expression) ex(errorMsgTemplate, params);
    }

    public static void isFalse(boolean expression) {
        if (expression) ex();
    }

    public static void isFalse(boolean expression, String errorMsgTemplate, Object... params) {
        if (expression) ex(errorMsgTemplate, params);
    }

    public static void equals(Object p1, Object p2) {
        if (!ObjectUtils.equals(p1, p2)) ex();
    }

    public static void equals(Object p1, Object p2, String errorMsgTemplate, Object... params) {
        if (!ObjectUtils.equals(p1, p2)) ex(errorMsgTemplate, params);
    }

    public static void notEquals(Object p1, Object p2) {
        if (ObjectUtils.equals(p1, p2)) ex();
    }

    public static void notEquals(Object p1, Object p2, String errorMsgTemplate, Object... params) {
        if (ObjectUtils.equals(p1, p2)) ex(errorMsgTemplate, params);
    }

    public static void isNull(Object object) {
        if (ObjectUtils.nonNull(object)) ex();
    }

    public static void isNull(Object object, String errorMsgTemplate, Object... params) {
        if (ObjectUtils.nonNull(object)) ex(errorMsgTemplate, params);
    }

    public static void nonNull(Object object) {
        if (ObjectUtils.isNull(object)) ex();
    }

    public static void nonNull(Object object, String errorMsgTemplate, Object... params) {
        if (ObjectUtils.isNull(object)) ex(errorMsgTemplate, params);
    }

    public static void isNotEmpty(Collection<?> collection) {
        if (ObjectUtils.isNull(collection)) ex();
    }

    public static void isNotEmpty(Collection<?> collection, String errorMsgTemplate, Object... params) {
        if (ObjectUtils.isNull(collection)) ex(errorMsgTemplate, params);
    }

    public static void isEmpty(Collection<?> collection) {
        if (ObjectUtils.nonNull(collection)) ex();
    }

    public static void isEmpty(Collection<?> collection, String errorMsgTemplate, Object... params) {
        if (ObjectUtils.nonNull(collection)) ex(errorMsgTemplate, params);
    }

    public static void isStrBlank(String obj) {
        if (ObjectUtils.nonNull(obj)) ex();
    }

    public static void isStrBlank(String obj, String errorMsgTemplate, Object... params) {
        if (ObjectUtils.nonNull(obj)) ex(errorMsgTemplate, params);
    }

    public static void isStrNotBlank(String obj) {
        if (ObjectUtils.isNull(obj)) ex();
    }

    public static void isStrNotBlank(String[] obj) {
        Arrays.stream(obj).forEach(Assert::isStrNotBlank);
    }

    public static void isStrNotBlank(String obj, String errorMsgTemplate, Object... params) {
        if (ObjectUtils.isNull(obj)) ex(errorMsgTemplate, params);
    }

    public static void isStrNotBlank(String[] obj, String errorMsgTemplate, Object... params) {
        Arrays.stream(obj).forEach(t -> isStrNotBlank(t, errorMsgTemplate, params));
    }

}
