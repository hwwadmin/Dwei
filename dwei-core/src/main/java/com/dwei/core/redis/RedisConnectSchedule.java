package com.dwei.core.redis;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RedisConnectSchedule {

    private final RedisSupport redisSupport;

    public RedisConnectSchedule(RedisSupport redisSupport) {
        this.redisSupport = redisSupport;
    }

    @Async
    @Scheduled(fixedDelay = 5000L)
    public void ping() {
        redisSupport.getRedisTemplate().hasKey("0");
    }

}
