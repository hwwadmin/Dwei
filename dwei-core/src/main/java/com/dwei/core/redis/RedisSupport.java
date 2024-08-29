package com.dwei.core.redis;

import com.dwei.core.redis.sorted.*;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * redis调用封装
 *
 * @author hww
 */
@Slf4j
@Getter
@Component
public class RedisSupport {

    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    private StringRedisSupport ops4str;
    private HashRedisSupport ops4hash;
    private ListRedisSupport ops4list;
    private SetRedisSupport ops4set;
    private ZSetRedisSupport ops4zset;
    private SpecialOpsRedisSupport ops4special;
    private BloomFilterRedisSupport bloomFilter;

    public RedisSupport(RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @PostConstruct
    public void init() {
        this.ops4str = new StringRedisSupport(this.stringRedisTemplate);
        this.ops4hash = new HashRedisSupport(this.redisTemplate);
        this.ops4list = new ListRedisSupport(this.redisTemplate);
        this.ops4set = new SetRedisSupport(this.stringRedisTemplate);
        this.ops4zset = new ZSetRedisSupport(this.stringRedisTemplate);
        this.ops4special = new SpecialOpsRedisSupport(this.redisTemplate);
        this.bloomFilter = new BloomFilterRedisSupport(this.redisTemplate);
    }

    public boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        Boolean ret = redisTemplate.expire(key, timeout, unit);
        return ret != null && ret;
    }

    public boolean del(final String key) {
        Boolean ret = redisTemplate.delete(key);
        return ret != null && ret;
    }

    public long del(final Collection<String> keys) {
        Long ret = redisTemplate.delete(keys);
        return ret == null ? 0 : ret;
    }

    public boolean isExist(final String key) {
        Boolean ret = redisTemplate.hasKey(key);
        return ret != null && ret;
    }

}
