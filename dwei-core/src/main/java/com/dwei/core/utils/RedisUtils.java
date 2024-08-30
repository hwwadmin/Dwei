package com.dwei.core.utils;

import com.dwei.core.redis.RedisSupport;

import java.util.Objects;

/**
 * redis工具类
 *
 * @author hww
 */
public abstract class RedisUtils {

    private static RedisSupport redisSupport;

    private RedisUtils() {

    }

    public static RedisSupport support() {
        if (Objects.nonNull(redisSupport)) return redisSupport;
        synchronized (RedisUtils.class) {
            if (Objects.nonNull(redisSupport)) return redisSupport;
            redisSupport = SpringContextUtils.getBean(RedisSupport.class);
            return redisSupport;
        }
    }

}
