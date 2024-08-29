package com.dwei.core.redis.sorted;

import com.dwei.common.utils.JsonUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

/**
 * redis有序集合数据类型操作支持
 *
 * @author hww
 */
public class ZSetRedisSupport {

    private final StringRedisTemplate redisTemplate;

    public ZSetRedisSupport(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Boolean add(final String key, final Object value, Integer score) {
        return redisTemplate.opsForZSet().add(key, JsonUtils.toJsonOrigin(value), score);
    }

    public Integer score(final String key, final Object value) {
        Double score = redisTemplate.opsForZSet().score(key, value);
        return score == null ? null : score.intValue();
    }

    public Set<ZSetOperations.TypedTuple<String>> getAll(final String key) {
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, 0, Integer.MAX_VALUE);
    }

    public Set<ZSetOperations.TypedTuple<String>> getAllByRange(final String key, final Integer start,
                                                                final Integer end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    public void incr(final String key, final Object value) {
        redisTemplate.opsForZSet().incrementScore(key, JsonUtils.toJsonOrigin(value), 1);
    }

}
