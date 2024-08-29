package com.dwei.core.redis.sorted;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis哈希数据类型操作支持
 *
 * @author hww
 */
@SuppressWarnings("unused")
public class HashRedisSupport {

    private final RedisTemplate<String, Object> redisTemplate;

    public HashRedisSupport(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Set<Object> keys(final String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    public void put(final String key, final String hKey, final Object value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    public void put(final String key, final String hKey, final Object value, long timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForHash().put(key, hKey, value);
        redisTemplate.expire(key, timeout, timeUnit);
    }

    public void putAll(final String key, final Map<Object, Object> values) {
        redisTemplate.opsForHash().putAll(key, values);
    }

    public void putAll(final String key, final Map<String, Object> values, final long timeout, final TimeUnit unit) {
        redisTemplate.opsForHash().putAll(key, values);
        redisTemplate.expire(key, timeout, unit);
    }

    public Object get(final String key, final String hKey) {
        return redisTemplate.opsForHash().get(key, hKey);
    }

    public Map<Object, Object> getAll(final String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public Object del(final String key, Object... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

    public List<Object> multiGet(final String key, final Collection<Object> hKeys) {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

}
