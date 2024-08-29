package com.dwei.core.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * 基于redis的分布式锁
 *
 * @author hww
 */
@Component
@Slf4j
public class RedisDistributedLock implements DistributedLock {

    private static final String lockSpace = "lock";

    private final StringRedisTemplate redisTemplate;

    public RedisDistributedLock(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private boolean lock(String key, long expire) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, lockSpace, expire, TimeUnit.MILLISECONDS);
        return success != null && success;
    }

    @Override
    public <T> T tryLock(String lockId, long keepMills, Supplier<T> supplier) {
        return tryLock(lockId, keepMills, 300, TimeUnit.MILLISECONDS, supplier);
    }

    @Override
    public <T> T tryLock(String lockId, long keepMills, long waitTime, Supplier<T> supplier) {
        return tryLock(lockId, keepMills, waitTime, TimeUnit.MILLISECONDS, supplier);
    }

    /**
     * redis 锁
     * 防止锁超时后释放别人的锁，释放锁
     *
     * @param lockId       redis key
     * @param keepMills    redis expire，millisecond
     * @param waitTime     try lock time
     * @param waitTimeUnit time unit
     * @param supplier     invoke method
     * @param <T>          supplier result type
     * @return supplier result
     */
    @Override
    public <T> T tryLock(String lockId, long keepMills, long waitTime, TimeUnit waitTimeUnit, Supplier<T> supplier) {
        AtomicReference<T> result = new AtomicReference<>();
        SpinBarrier.spin(() -> {
            boolean isLock = false; // 是否获得锁
            var start = System.currentTimeMillis();
            try {
                if (lock(lockId, keepMills)) {
//                    log.info("锁成功:{}", lockId);
                    isLock = true;
                    result.set(supplier.get());
                    return true;
                }
            } catch (Exception e) {
                log.error("锁异常:{}", lockId, e);
                throw new RuntimeException(e);
            } finally {
                // 未获得不释放锁
                if (isLock) {
                    // 超过锁过期时间不释放锁，防止释放了其他人获得的锁
                    var end = System.currentTimeMillis();
                    if (end - start + 100 < keepMills) redisTemplate.delete(lockId);
                }
            }
            return false;
        }, waitTimeUnit.toMillis(waitTime));
        return result.get();
    }

}
