package com.dwei.core.redis;

import com.dwei.common.constants.AppConstants;
import com.dwei.common.exception.BizException;
import com.dwei.common.exception.UtilsException;
import com.dwei.common.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * redis 限流器，通过 令牌桶算法实现
 */
@Service
@Slf4j
public class RedisRateLimiter {

    public final StringRedisTemplate redisTemplate;
    private final RedisScript<List<Long>> script;
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public RedisRateLimiter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        DefaultRedisScript<List<Long>> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(
                new ResourceScriptSource(new ClassPathResource("script/rate_limiter.lua")));
        //noinspection unchecked
        redisScript.setResultType((Class<List<Long>>) (Class<?>) List.class);
        this.script = redisScript;
        this.initialized.compareAndSet(false, true);
    }

    protected List<String> getKeys(String id) {
        // use `{}` around keys to use Redis Key hash tags
        // this allows for using redis cluster

        // Make a unique key per user.
        String prefix = "limit" + AppConstants.MARK_COLON + id;

        // You need two Redis keys for Token Bucket.
        String tokenKey = prefix + AppConstants.MARK_COLON + "tokens";
        String timestampKey = prefix + AppConstants.MARK_COLON + "timestamp";
        return Arrays.asList(tokenKey, timestampKey);
    }

    /**
     * 是否未被限流，是否允许
     *
     * @param key             限流的 key
     * @param replenishRate   令牌补充频率，每秒多少个
     * @param burstCapacity   桶容量
     * @param requestedTokens 请求令牌数量
     * @return true 未被限流，false 已限流
     */
    public boolean isAllowed(String key, int replenishRate, int burstCapacity, int requestedTokens) {
        if (!this.initialized.get()) {
            throw new IllegalStateException("RedisRateLimiter is not initialized");
        }
        try {
            List<String> keys = getKeys(key);
            // The arguments to the LUA script. time() returns unixtime in seconds.
            String[] scriptArgs = new String[]{replenishRate + "", burstCapacity + "", "", requestedTokens + ""};
            // allowed, tokens_left = redis.eval(SCRIPT, keys, args)
            List<Long> list = this.redisTemplate.execute(this.script, keys, scriptArgs);
            return Optional.ofNullable(CollectionUtils.first(list)).orElse(0L) == 1L;
        } catch (Exception e) {
            log.error("redis限流异常", e);
        }
        return false;
    }

    /**
     * 等待获取令牌后执行
     *
     * @param key             限流的 key
     * @param replenishRate   令牌补充频率，每秒多少个
     * @param burstCapacity   桶容量
     * @param requestedTokens 请求令牌数量
     * @param ttl             等待时长
     * @param supplier        获取令牌后执行
     * @throws BizException     限流等待超时
     * @throws RuntimeException sleep interrupt
     */
    public <T> T acquire(String key, int replenishRate, int burstCapacity, int requestedTokens, int ttl,
                         Supplier<T> supplier) {
        if (key == null || key.isBlank()) {
            throw UtilsException.exception("redis rate limiter key must not blank");
        }
        long start = System.currentTimeMillis();
        long sleep = 1000 / replenishRate;
        if (sleep == 0) sleep = 100;
        while (!isAllowed(key, replenishRate, burstCapacity, requestedTokens)) {
            long finish = System.currentTimeMillis();
            if (finish + sleep - start > ttl) {
                throw BizException.exception("已被限流，请等待");
            }
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return supplier.get();
    }

}
