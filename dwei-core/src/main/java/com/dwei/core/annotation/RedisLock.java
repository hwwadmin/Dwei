package com.dwei.core.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {
    /**
     * key must not empty
     * 支持 spring spel, 当包含 # 符号，才解析 spel
     */
    String key();

    /**
     * 锁过期时间
     */
    long expire() default 10000L;

    /**
     * 等待时长
     */
    long waitTime() default 300L;

    /**
     * 等待时长单位
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
