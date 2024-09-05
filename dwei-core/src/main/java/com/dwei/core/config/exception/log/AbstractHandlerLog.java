package com.dwei.core.config.exception.log;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.dwei.common.exception.IllegalValidatedException;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.ImmutableSet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 抽象的日志处理器
 *
 * @author hww
 */
public abstract class AbstractHandlerLog implements ExceptionHandlerLog {

    /**
     * 忽略的异常类型
     */
    private static final Set<Class<? extends Exception>> ignore = ImmutableSet.<Class<? extends Exception>>builder()
            .add(IllegalValidatedException.class)
            .add(IllegalArgumentException.class)
            .add(MethodArgumentNotValidException.class)
            .build();

    /**
     * 日志栈限流缓存
     */
    private final Cache<String, String> rateLimitCache = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(1, TimeUnit.SECONDS)
            .build();

    @SuppressWarnings({"unchecked"})
    @Override
    public void log(HttpServletRequest request, Throwable ex) {
        // 忽略的异常类型
        for (Class<? extends Exception> clazz : ignore) {
            if (ExceptionUtil.isCausedBy(ex, clazz)) return;
        }

        // 限流
        if (enableLimit()) {
            // 对日志栈限流，防止短时间内重复异常大量刷入
            StackTraceElement traceElement = ex.getStackTrace()[ex.getStackTrace().length - 1];
            String key = traceElement.getFileName() + ":" +
                    traceElement.getClassName() + ":" +
                    traceElement.getMethodName() + ":" +
                    traceElement.getLineNumber();
            if (rateLimitCache.getIfPresent(key) != null) return;
            rateLimitCache.put(key, "");
        }

        // 日志处理
        doLogHandle(new ContentCachingRequestWrapper(request), ex);
    }

    /**
     * 是否启用限流
     */
    protected boolean enableLimit() {
        return true;
    }

    /**
     * 日志处理
     */
    public abstract void doLogHandle(ContentCachingRequestWrapper request, Throwable ex);

}
