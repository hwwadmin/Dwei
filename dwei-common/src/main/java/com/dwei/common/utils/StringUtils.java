package com.dwei.common.utils;

import com.dwei.common.constants.AppConstants;

import java.util.Collection;

/**
 * String工具类
 *
 * @author hww
 */
public abstract class StringUtils {

    public static boolean isBlank(CharSequence str) {
        return !isNotBlank(str);
    }

    public static boolean isNotBlank(CharSequence str) {
        return org.apache.commons.lang3.StringUtils.isNotBlank(str);
    }

    public static String join(Collection<String> collection) {
        return String.join(AppConstants.STRING_CSV, collection);
    }

    public static String join(Collection<String> collection, String delimiter) {
        return String.join(delimiter, collection);
    }

    public static boolean hasText(CharSequence str) {
        return isNotBlank(str) && containsText(str);
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();

        for (int i = 0; i < strLen; ++i) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    public static String trim(CharSequence str) {
        return cn.hutool.core.util.StrUtil.trim(str);
    }

    public static String format(CharSequence template, Object... params) {
        return cn.hutool.core.util.StrUtil.format(template, params);
    }

    public static String toCamelCase(CharSequence name) {
        return cn.hutool.core.util.StrUtil.toCamelCase(name);
    }

}
