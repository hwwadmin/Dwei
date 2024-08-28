package com.dwei.common.utils;

import com.dwei.common.function.Execute;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 安全操作工具类
 *
 * @author hww
 */
@Slf4j
public abstract class SafeUtils {

    public static <T> @Nullable T get(Supplier<T> supplier) {
        return get(supplier, null);
    }

    public static <T> @Nullable T get(Supplier<T> supplier, T defaultValue) {
        if (Objects.isNull(supplier)) return defaultValue;
        try {
            return supplier.get();
        } catch (Throwable e) {
            log.error("SafeUtils#get", e);
            return defaultValue;
        }
    }

    public static <T> @Nullable T getAndIgnoreError(Supplier<T> supplier) {
        return getAndIgnoreError(supplier, null);
    }

    public static <T> @Nullable T getAndIgnoreError(Supplier<T> supplier, T defaultValue) {
        // 认知到函数执行的过程会出现异常，而且这个异常是业务允许不需要处理的情况下可使用
        if (Objects.isNull(supplier)) return defaultValue;
        try {
            return supplier.get();
        } catch (Throwable e) {
            return defaultValue;
        }
    }

    public static void apply(Execute execute) {
        if (Objects.isNull(execute)) return;
        try {
            execute.apply();
        } catch (Exception e) {
            log.error("SafeUtils#apply", e);
        }
    }

    public static <T> void consumer(T obj, Consumer<T> consumer) {
        if (Objects.isNull(obj)) return;
        if (Objects.isNull(consumer)) return;
        try {
            consumer.accept(obj);
        } catch (Exception e) {
            log.error("SafeUtils#consumer", e);
        }
    }

}
