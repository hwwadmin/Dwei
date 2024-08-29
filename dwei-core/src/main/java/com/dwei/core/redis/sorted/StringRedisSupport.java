package com.dwei.core.redis.sorted;

import com.dwei.common.utils.JsonUtils;
import com.dwei.common.utils.Maps;
import com.dwei.common.utils.ObjectUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * redis字符型数据类型操作支持
 */
public class StringRedisSupport {

    private final StringRedisTemplate redisTemplate;

    public StringRedisSupport(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void set(final String key, final Object value) {
        redisTemplate.opsForValue().set(key, JsonUtils.toJson(value));
    }

    public void set(final String key, final Object value, final long timeout) {
        String json;
        if (value instanceof String s) {
            json = s;
        } else {
            json = JsonUtils.toJson(value);
        }
        redisTemplate.opsForValue().set(key, json, timeout, TimeUnit.SECONDS);
    }

    public void set(final String key, final Object value, final long timeout, TimeUnit timeUtil) {
        redisTemplate.opsForValue().set(key, JsonUtils.toJson(value), timeout, timeUtil);
    }

    public void multiSet(Map<String, Object> map, final long timeout, TimeUnit timeUtil) {
        if (timeout <= 0) {
            multiSet(map);
            return;
        }

        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        Map<String, String> result = Maps.map(map, JsonUtils::toJson);
        opsForValue.multiSet(result);
        StringRedisSerializer keySerializer = (StringRedisSerializer) redisTemplate.getKeySerializer();
        var valueSerializer = (StringRedisSerializer) redisTemplate.getValueSerializer();
        redisTemplate.executePipelined((RedisCallback<String>) connection -> {
            for (Map.Entry<String, Object> next : map.entrySet()) {
                String mapKey = next.getKey();
                byte[] key = keySerializer.serialize(mapKey);
                byte[] value;
                if (next.getValue() instanceof String s) {
                    value = valueSerializer.serialize(s);
                } else
                    value = valueSerializer.serialize(JsonUtils.toJsonOrigin(next.getValue()));
                assert value != null;
                assert key != null;
                try {
                    connection.pSetEx(key, TimeoutUtils.toMillis(timeout, timeUtil), value);
                } catch (Exception e) {
                    connection.setEx(key, TimeoutUtils.toSeconds(timeout, timeUtil), value);
                }
            }
            return null;
        });
    }

    public void multiSet(Map<String, Object> map) {
        var stringMap = Maps.map(map, JsonUtils::toJsonOrigin);
        redisTemplate.opsForValue().multiSet(stringMap);
    }

    public String get(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public String getAndExpire(final String key, final long timeout, final TimeUnit timeUnit) {
        try {
            return redisTemplate.opsForValue().getAndExpire(key, timeout, timeUnit);
        } catch (Exception e) {
            return redisTemplate.opsForValue().getAndExpire(key, timeout, timeUnit);
        }
    }

    public <T> T getAndExpire(final String key, Type type, final long timeout, final TimeUnit timeUnit) {
        if (timeout <= 0) return get(key, type);
        var result = getAndExpire(key, timeout, timeUnit);
        return JsonUtils.format(result, type);
    }

    public <T> T get(final String key, Type type) {
        String o = get(key);
        return JsonUtils.format(o, type);
    }

    public <T> T get(final String key, Class<T> type) {
        String o = get(key);
        return JsonUtils.format(o, type);
    }

    public String getString(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Integer getInteger(final String key) {
        String s = redisTemplate.opsForValue().get(key);
        if (ObjectUtils.isNull(s)) return null;
        return Integer.valueOf(s);
    }

    public Long getLong(final String key) {
        String s = redisTemplate.opsForValue().get(key);
        if (ObjectUtils.isNull(s)) return null;
        return Long.valueOf(s);
    }

    public List<String> multiGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    public <T> List<T> multiGet(Collection<String> keys, Type type) {
        List<String> value = multiGet(keys);
        //noinspection unchecked
        return value.stream().map(t -> (T) JsonUtils.format(t, type)).collect(Collectors.toList());
    }

    public <T> List<T> multiGet(Collection<String> keys, Class<T> type) {
        List<String> value = multiGet(keys);
        return value.stream().map(t -> (T) JsonUtils.format(t, type)).collect(Collectors.toList());
    }

}
