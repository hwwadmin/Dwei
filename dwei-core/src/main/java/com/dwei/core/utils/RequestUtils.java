package com.dwei.core.utils;

import com.dwei.common.utils.Assert;
import com.dwei.common.utils.JsonUtils;
import com.dwei.common.utils.ObjectUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * HTTP请求解析工具类
 *
 * @author hww
 */
public class RequestUtils {

    private static final Set<String> IGNORE_HEADER_NAMES =
            Collections.newSetFromMap(new LinkedCaseInsensitiveMap<>(10, Locale.ENGLISH));

    static {
        IGNORE_HEADER_NAMES.add("Accept");
        IGNORE_HEADER_NAMES.add("Accept-Encoding");
        IGNORE_HEADER_NAMES.add("Accept-Language");
        IGNORE_HEADER_NAMES.add("Connection");
        IGNORE_HEADER_NAMES.add("Host");
        IGNORE_HEADER_NAMES.add("Origin");
        IGNORE_HEADER_NAMES.add("Referer");
        IGNORE_HEADER_NAMES.add("sec-ch-ua");
        IGNORE_HEADER_NAMES.add("sec-ch-ua-mobile");
        IGNORE_HEADER_NAMES.add("sec-ch-ua-platform");
        IGNORE_HEADER_NAMES.add("Sec-Fetch-Dest");
        IGNORE_HEADER_NAMES.add("Sec-Fetch-Mode");
        IGNORE_HEADER_NAMES.add("Sec-Fetch-Site");
        IGNORE_HEADER_NAMES.add("User-Agent");

        IGNORE_HEADER_NAMES.add("X-Forwarded-Host");
        IGNORE_HEADER_NAMES.add("X-Forwarded-Port");
        IGNORE_HEADER_NAMES.add("X-Forwarded-Proto");
        IGNORE_HEADER_NAMES.add("X-Forwarded-Prefix");
        IGNORE_HEADER_NAMES.add("X-Forwarded-Ssl");
    }

    public static ServletRequestAttributes getServlet() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    public static HttpServletRequest getRequest() {
        return getServlet().getRequest();
    }

    /**
     * HttpServletRequest的包装加强
     * 推荐使用该方法去获取请求参数
     */
    public static ContentCachingRequestWrapper getRequestWrapper() {
        return new ContentCachingRequestWrapper(getRequest());
    }

    public static HttpServletResponse getResponse() {
        return getServlet().getResponse();
    }

    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 获取http的head参数
     */
    public static Map<String, List<String>> getHeadParam(ContentCachingRequestWrapper request) {
        Map<String, List<String>> headers = new LinkedCaseInsensitiveMap<>(Locale.SIMPLIFIED_CHINESE);
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            if (!IGNORE_HEADER_NAMES.contains(name)) {
                headers.put(name, Collections.list(request.getHeaders(name)));
            }
        }
        return headers;
    }

    public static Map<String, List<String>> getHeadParam() {
        return getHeadParam(getRequestWrapper());
    }

    public static String getHeadParamStr() {
        return JsonUtils.bean2JsonStr(getHeadParam());
    }

    public static String getHeadParamStr(ContentCachingRequestWrapper request) {
        return JsonUtils.bean2JsonStr(getHeadParam(request));
    }

    /**
     * 获取http的请求参数
     */
    public static Map<String, String[]> getRequestParam(ContentCachingRequestWrapper request) {
        return request.getParameterMap();
    }

    public static Map<String, String[]> getRequestParam() {
        return getRequestParam(getRequestWrapper());
    }

    public static String getRequestParamStr() {
        return JsonUtils.bean2JsonStr(getRequestParam());
    }

    public static String getRequestParamStr(ContentCachingRequestWrapper request) {
        return JsonUtils.bean2JsonStr(getRequestParam(request));
    }

    public static String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    public static String getParameter(String name, String defaultValue) {
        var str = getParameter(name);
        return ObjectUtils.nonNull(str) ? str : defaultValue;
    }

    /**
     * 获取http的body参数
     */
    public static String getBodyParam(ContentCachingRequestWrapper request) {
        byte[] contentAsByteArray = request.getContentAsByteArray();
        if (ObjectUtils.isNull(contentAsByteArray)) {
            try {
                contentAsByteArray = request.getInputStream().readAllBytes();
                if (ObjectUtils.isNull(contentAsByteArray)) return null;
            } catch (IOException e) {
                return null;
            }
        }
        return new String(contentAsByteArray, StandardCharsets.UTF_8);
    }

    public static String getBodyParam() {
        return getBodyParam(getRequestWrapper());
    }

    public static String getUri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    public static String getMethod(HttpServletRequest request) {
        return request.getMethod();
    }

    public static boolean isSameMethod(String method1, String method2) {
        Assert.nonNull(method1);
        Assert.nonNull(method2);
        return method1.equalsIgnoreCase(method2);
    }

}
