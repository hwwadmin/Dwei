package com.dwei.core.redis;

import cn.hutool.core.util.RandomUtil;
import com.dwei.common.utils.Assert;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.common.utils.ReflectUtils;
import com.dwei.core.lock.DistributedLock;
import com.dwei.core.utils.RedisUtils;
import com.dwei.core.utils.SpringContextUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 基于redis实现旁路缓存策略CacheAside
 *
 * @author hww
 */
@Slf4j
public class RedisCacheAside<T> {

    private static final String cache_template = "ca:%s:c:%s";
    private static final String lock_template = "ca:%s:l:%s";

    private RedisSupport redisSupport;
    private DistributedLock lock;

    private String serviceName;
    private Class<T> clazz;
    private Long timeout;

    /** 缓存标识code构造函数 */
    private Function<T, String> codeBuild;
    /** 根据缓存标识code拉取对应数据函数 */
    private Function<String, Optional<T>> pullData;

    private RedisCacheAside() {

    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {

        private final RedisCacheAside<T> redisCacheAside;

        public Builder() {
            this.redisCacheAside = new RedisCacheAside<>();
            this.redisCacheAside.redisSupport = RedisUtils.support();
            this.redisCacheAside.lock = SpringContextUtils.getBean(DistributedLock.class);
        }

        public Builder<T> serviceName(String serviceName) {
            this.redisCacheAside.serviceName = serviceName;
            return this;
        }

        public Builder<T> type(Class<T> clazz) {
            this.redisCacheAside.clazz = clazz;
            return this;
        }

        public Builder<T> timeout(long timeout) {
            this.redisCacheAside.timeout = timeout;
            return this;
        }

        public Builder<T> pullData(Function<String, Optional<T>> pullData) {
            this.redisCacheAside.pullData = pullData;
            return this;
        }

        public Builder<T> codeBuild(Function<T, String> codeBuild) {
            this.redisCacheAside.codeBuild = codeBuild;
            return this;
        }

        private void verify() {
            Assert.isStrNotBlank(this.redisCacheAside.serviceName, "服务名为空");
            Assert.nonNull(this.redisCacheAside.clazz, "缓存对象类型为空");
            Assert.nonNull(this.redisCacheAside.codeBuild, "缓存标识code构造函数为空");
            Assert.nonNull(this.redisCacheAside.pullData, "根据缓存标识code拉取对应数据函数为空");

            if (Objects.isNull(this.redisCacheAside.timeout))
                this.redisCacheAside.timeout = RandomUtil.randomLong(1728000000L, 2592000000L);
        }

        public RedisCacheAside<T> build() {
            verify();
            return this.redisCacheAside;
        }

    }

    private String getCacheKey(String code) {
        return redisSupport.format(String.format(cache_template, serviceName, code));
    }

    private String getLockKey(String code) {
        return redisSupport.format(String.format(lock_template, serviceName, code));
    }

    public Optional<T> get(String code) {
        if (ObjectUtils.isNull(code)) return Optional.empty();
        var cacheKey = getCacheKey(code);
        T e1 = this.redisSupport.getOps4str().get(cacheKey, clazz);
        if (Objects.isNull(e1)) {
            // 数据同步过程上锁，防止缓存击穿
            try {
                return lock.tryLock(getLockKey(code), 100, () -> {
                    // 再去读取一次缓存，如果有读取到直接返回
                    T e2 = this.redisSupport.getOps4str().get(cacheKey, clazz);
                    // 读缓存成功说明已经重新写过缓存了不需要再去查数据库直接返回
                    if (Objects.nonNull(e2)) return Optional.of(e2);
                    // 没有读到缓存的话进行数据获取
                    Optional<T> optional = pullData.apply(code);
                    if (optional.isEmpty()) {
                        // 数据不存在的时候写一个空对象到缓存，这个缓存持续3s，防止缓存穿透
                        this.redisSupport.getOps4str().set(cacheKey, ReflectUtils.newInstance(clazz), 3L, TimeUnit.SECONDS);
                        return optional;
                    }
                    // 成功读取到对应数据
                    e2 = optional.get();
                    this.redisSupport.getOps4str().set(cacheKey, e2, timeout, TimeUnit.MILLISECONDS);
                    return optional;
                });
            } catch (Exception e) {
                log.error("[{}]缓存数据同步失败, id:[{}]", serviceName, code);
                throw e;
            }
        }
        // 存在的时候需要校验缓存对象是否有code，如果code是个空的说明是个防止穿透的空数据
        return ObjectUtils.nonNull(codeBuild.apply(e1)) ? Optional.of(e1) : Optional.empty();
    }

    public List<T> list(Collection<String> codes) {
        if (ObjectUtils.isNull(codes)) return Lists.newArrayList();
        var keys = codes.stream().map(this::getCacheKey).toList();
        List<T> data = this.redisSupport.getOps4str().multiGet(keys, clazz).stream().filter(Objects::nonNull).collect(Collectors.toList());
        // 数据长度一样的话说明都从缓存查询到直接返回
        if (Objects.equals(keys.size(), data.size())) return data;
        // 获取未查询到数据的id列表
        var map = data.stream().collect(Collectors.toMap(t -> codeBuild.apply(t), ObjectUtils::self));
        codes.stream()
                .filter(Objects::nonNull)
                .filter(code -> !map.containsKey(code))
                .map(code -> {
                    Optional<T> optional = get(code);
                    return optional.orElse(null);
                })
                .filter(Objects::nonNull)
                .forEach(data::add);
        return data;
    }

    public void del(String code) {
        del(Lists.newArrayList(code));
    }

    public void del(Iterable<String> codes) {
        this.redisSupport.del(Lists.newArrayList(codes).stream().map(this::getCacheKey).collect(Collectors.toList()));
    }

}
