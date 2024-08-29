package com.dwei.core.redis.sorted;

import com.dwei.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * redis特殊操作支持
 *
 * @author hww
 */
@SuppressWarnings("unused")
@Slf4j
public class SpecialOpsRedisSupport {

    private final RedisTemplate<String, Object> redisTemplate;

    public SpecialOpsRedisSupport(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 模糊匹配前缀删除
     */
    public void delByPre(String pre) {
        if (!StringUtils.hasText(pre)) return;
        var keys = keys(pre + "*");
        if (!CollectionUtils.isEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 模糊匹配
     */
    public Map<String, Integer> findByPre(String prefix) {
        if (!StringUtils.hasText(prefix)) return null;
        Map<String, Integer> ret = new HashMap<>();
        var keys = keys(prefix + "*");
        assert keys != null;
        for (String key : keys) {
            Object v = redisTemplate.opsForValue().get(key);
            ret.put(key, (Integer) v);
        }
        return ret;
    }

    /**
     * 获取符合条件的key
     * redis keys 方法数据量大的时候 会阻塞redis线程
     * 使用 scan 替代
     *
     * @param pattern 表达式
     */
    public List<String> keys(String pattern) {
        return keys(pattern, 10000L);
    }

    public List<String> keys(String pattern, long count) {
        List<String> keys = new ArrayList<>();
        this.scan(pattern, count, item -> {
            //符合条件的key
            String key = new String(item, StandardCharsets.UTF_8);
            keys.add(key);
        });
        return keys;
    }

    /**
     * scan 实现
     *
     * @param pattern  表达式
     * @param consumer 对迭代到的key进行操作
     */
    public void scan(String pattern, Consumer<byte[]> consumer) {
        scan(pattern, 10000L, consumer);
    }

    public void scan(String pattern, long count, Consumer<byte[]> consumer) {
        this.redisTemplate.execute((RedisConnection connection) -> {
            Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().count(count).match(pattern).build());
            cursor.forEachRemaining(consumer);
            return null;
        });
    }

    /**
     * 初始化自增长值
     */
    public void setIncr(String key, Long value) {
        RedisAtomicLong counter = new RedisAtomicLong(key,
                Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        counter.set(value);
    }

    /**
     * 获取自增长值
     */
    public Long getIncr(String key) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key,
                Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        return entityIdCounter.getAndIncrement();
    }

    /**
     * Redis原子自增操作, 更适合一次性使用的场景
     */
    public Long incr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * Redis原子自增操作, 更适合一次性使用的场景
     */
    public Long incr(String key, final long timeout, final TimeUnit unit) {
        Long result = redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, timeout, unit);
        return result;
    }

    /**
     * Redis原子自增操作, 更适合一次性使用的场景
     *
     * @param key   key
     * @param delta 步长
     * @return 自增后的值
     */
    public Long incr(String key, Long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * Redis原子自增操作, 更适合一次性使用的场景
     *
     * @param key   key
     * @param delta 步长
     * @return 自增后的值
     */
    public Long incr(String key, Long delta, final long timeout, final TimeUnit unit) {
        Long result = redisTemplate.opsForValue().increment(key, delta);
        redisTemplate.expire(key, timeout, unit);
        return result;
    }

}
