package com.dwei.core.utils;

import com.dwei.common.utils.ObjectUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Arrays;
import java.util.Locale;

/**
 * 本地化类型工具类
 *
 * @author hww
 */
public class LocaleUtils {

    private static final String LANG_KEY = "LANG";

    private LocaleUtils() {

    }

    /**
     * 初始化Locale
     */
    public static void initLocale(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies(); // 从cookie读取本地化类型
        Locale locale = ArrayUtils.isNotEmpty(cookies) ?
                Arrays.stream(cookies)
                        .filter(cookie -> cookie.getName().equals(LANG_KEY))
                        .map(Cookie::getValue)
                        .findFirst()
                        .map(LocaleUtils::parseLocale)
                        .orElse(Locale.ENGLISH) : Locale.ENGLISH;
        setLocale(locale, request, response);
    }

    /**
     * 设置web请求的Locale
     */
    public static void setLocale(Locale locale, HttpServletRequest request, HttpServletResponse response) {
        if (ObjectUtils.nonNull(request)) {
            // 设置web请求的Locale
            var localeResolver = RequestContextUtils.getLocaleResolver(request);
            if (ObjectUtils.nonNull(localeResolver)) localeResolver.setLocale(request, response, locale);
        }
        setLocale(locale);
    }

    /**
     * 设置线程Locale
     */
    public static void setLocale(Locale locale) {
        LocaleContextHolder.setLocale(locale);
    }

    /**
     * 读取线程Locale
     */
    public static Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }

    /**
     * 解析Locale字符串
     */
    public static Locale parseLocale(String localeValue) {
        return StringUtils.parseLocale(localeValue);
    }

}
