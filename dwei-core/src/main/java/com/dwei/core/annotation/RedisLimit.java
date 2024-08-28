package com.dwei.core.annotation;

import java.lang.annotation.*;

@SuppressWarnings("unused")
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLimit {

    /**
     * key must not empty
     * 支持 spring spel, 当包含 # 符号，才解析 spel
     */
    String key();

    /**
     * 令牌补充频率，每秒多少个
     */
    int replenishRate() default 1;

    /**
     * 桶容量
     */
    int burstCapacity() default 1;

    /**
     * 请求令牌数量
     */
    int requestedTokens() default 1;

    /**
     * 等待时长
     */
    int ttl() default 1000;

}
