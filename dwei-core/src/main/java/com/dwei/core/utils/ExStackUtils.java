package com.dwei.core.utils;

import cn.hutool.core.io.FastByteArrayOutputStream;
import com.dwei.common.utils.ObjectUtils;
import jakarta.servlet.http.HttpServletRequest;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * 异常堆栈工具类
 *
 * @author hww
 */
public abstract class ExStackUtils {

    public static final String BASE_PACKAGE;
    private static final String EXCEPTION_TAG = "exception";

    static {
        String packageName = ExStackUtils.class.getPackageName();
        var first = packageName.indexOf('.');
        var second = packageName.indexOf('.', first + 1);
        var third = packageName.indexOf('.', second + 1);
        BASE_PACKAGE = packageName.substring(0, third);
    }

    public static String getStack(HttpServletRequest request) {
        var ex = ExStackUtils.getException(request);
        return getStack(ex);
    }

    public static String getStack(Throwable throwable) {
        if (throwable == null) return null;
        final FastByteArrayOutputStream baos = new FastByteArrayOutputStream();
        throwable.printStackTrace(new PrintStream(baos));
        final String exceptionStr = baos.toString();
        if (ObjectUtils.isNull(exceptionStr)) return exceptionStr;
        String[] lines = exceptionStr.split("\n");
        var repeatLine = new HashSet<String>();
        return Arrays.stream(lines)
                .filter(line -> {
                    // 去除相同异常的行
                    if (repeatLine.contains(line)) {
                        return false;
                    }
                    repeatLine.add(line);
                    return true;
                })
                .filter(line -> {
                    //noinspection StringEquality
                    if (lines[0] == line) {
                        // 保留第一行
                        return true;
                    }
                    // 排除不是自己包的异常
                    return line.contains(ExStackUtils.BASE_PACKAGE);
                })
                .collect(Collectors.joining("\n"));
    }

    public static void putException(HttpServletRequest request, Exception ex) {
        request.setAttribute(EXCEPTION_TAG, ex);
    }

    public static Exception getException(HttpServletRequest request) {
        return (Exception) request.getAttribute(EXCEPTION_TAG);
    }

}
