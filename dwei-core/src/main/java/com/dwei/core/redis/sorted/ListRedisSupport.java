package com.dwei.core.redis.sorted;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.List;

/**
 * redis列表数据类型操作支持
 *
 * @author hww
 */
public class ListRedisSupport {

    private final RedisTemplate<String, Object> redisTemplate;

    public ListRedisSupport(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public long leftPush(final String key, final Object value) {
        Long count = redisTemplate.opsForList().leftPush(key, value);
        return count == null ? 0 : count;
    }

    public long leftPushAll(final String key, final Collection<?> values) {
        Long count = redisTemplate.opsForList().leftPushAll(key, values);
        return count == null ? 0 : count;
    }

    public Object leftPop(final String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    public long rightPush(final String key, final Object value) {
        Long count = redisTemplate.opsForList().rightPush(key, value);
        return count == null ? 0 : count;
    }

    public long rightPushAll(final String key, final Collection<?> values) {
        Long count = redisTemplate.opsForList().rightPushAll(key, values);
        return count == null ? 0 : count;
    }

    public Object rightPop(final String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    public List<Object> rightPop(final String key, final long count) {
        return redisTemplate.opsForList().rightPop(key, count);
    }

    public List<Object> range(final String key, final int start, final int end) {
        // start=0，end=-1表示获取全部元素
        return redisTemplate.opsForList().range(key, start, end);
    }

    public void del(final String key, final Object value) {
        redisTemplate.opsForList().remove(key, 1, value);
    }

    public void trim(final String key, int start, int end) {
        redisTemplate.opsForList().trim(key, start, end);
    }

    public Long size(final String key) {
        return redisTemplate.opsForList().size(key);
    }

}
