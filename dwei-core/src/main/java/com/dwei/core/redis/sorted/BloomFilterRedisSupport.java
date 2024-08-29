package com.dwei.core.redis.sorted;

import cn.hutool.bloomfilter.BitSetBloomFilter;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;

/**
 * 布隆过滤器
 */
public class BloomFilterRedisSupport {
    private static final int DEFAULT_HASH_SIZE = 8;
    private static final int DEFAULT_SIZE = 1000000;
    private final RedisTemplate<String, Object> redisTemplate;

    public BloomFilterRedisSupport(RedisTemplate<String, Object> redisTemplate) {this.redisTemplate = redisTemplate;}

    /**
     * 根据给定的布隆过滤器添加值
     */
    public <T> void put(int hashTimes, int size, String key, String value) {
        int[] hashes = BitSetBloomFilter.createHashes(value, hashTimes);
        for (int i : hashes) {
            int position = Math.abs(i % size);
            redisTemplate.opsForValue().setBit(key, position, true);
        }
    }

    /**
     * 根据给定的布隆过滤器添加值
     */
    public <T> void put(String key, String value) {
        put(DEFAULT_HASH_SIZE, DEFAULT_SIZE / DEFAULT_HASH_SIZE, key, value);
    }

    /**
     * 根据给定的布隆过滤器判断值是否存在
     */
    public <T> boolean contain(int hashTimes, int size, String key, String value) {
        int[] hashes = BitSetBloomFilter.createHashes(value, hashTimes);
        for (int i : hashes) {
            int position = Math.abs(i % size);
            return !Objects.equals(Boolean.TRUE, redisTemplate.opsForValue().getBit(key, position));
        }
        return true;
    }

    /**
     * 根据给定的布隆过滤器判断值是否存在
     */
    public <T> boolean contain(String key, String value) {
        return contain(DEFAULT_HASH_SIZE, DEFAULT_SIZE / DEFAULT_HASH_SIZE, key, value);
    }
}
