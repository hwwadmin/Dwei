package com.dwei.core.redis.sorted;

import com.dwei.common.utils.JsonUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * redis集合数据类型操作支持
 *
 * @author hww
 */
public class SetRedisSupport {

    private final StringRedisTemplate redisTemplate;

    public SetRedisSupport(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public long add(final String key, final String... values) {
        Long count = redisTemplate.opsForSet().add(key, values);
        return count == null ? 0 : count;
    }

    public <T> Set<T> get(String key, Class<T> tClass) {
        Set<String> set = redisTemplate.opsForSet().members(key);
        if (set == null) return null;
        if (String.class.equals(tClass)) //noinspection unchecked
            return (Set<T>) set;
        return set.stream().map(item -> JsonUtils.format(item, tClass)).collect(Collectors.toSet());
    }

    public Set<String> get(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public long del(final String key, final String... values) {
        //noinspection ConfusingArgumentToVarargsMethod
        Long count = redisTemplate.opsForSet().remove(key, values);
        return count == null ? 0 : count;
    }

    public long size(final String key) {
        Long size = redisTemplate.opsForSet().size(key);
        return size == null ? 0L : size;
    }

}
