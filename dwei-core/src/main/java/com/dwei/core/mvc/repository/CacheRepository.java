package com.dwei.core.mvc.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dwei.common.utils.Assert;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.lock.DistributedLock;
import com.dwei.core.mvc.pojo.entity.BaseEntity;
import com.dwei.core.redis.RedisCacheAside;
import com.dwei.core.utils.RedisUtils;
import com.dwei.core.utils.SpringContextUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * 缓存资源仓库 Repository
 * 基于{@link RedisCacheAside}旁路缓存策略自动构造缓存资源仓库，简化使用
 *
 * @author hww
 */
public class CacheRepository<M extends BaseMapper<T>, T extends BaseEntity> {

    private static final String LOCK_KEY_FM = "cr:%s:refresh:lock";
    private static final String EXIST_KEY_FM = "cr:%s:refresh:exist";

    private final IBaseRepository<M, T> repository;
    private final RedisCacheAside<T> cacheAside;

    /**
     * 服务名称
     */
    private final String serviceName;

    public CacheRepository(IBaseRepository<M, T> repository, String serviceName) {
        Assert.nonNull(repository);
        Assert.isStrNotBlank(serviceName);

        this.serviceName = serviceName;
        this.repository = repository;
        this.cacheAside = RedisCacheAside.<T>builder()
                .serviceName(serviceName)
                .type(this.repository.getEntityClass())
                .codeBuild(t -> String.valueOf(t.getId()))
                .pullData(code -> repository.getOptById(Long.valueOf(code)))
                .build();
    }

    public CacheRepository(IBaseRepository<M, T> repository, String serviceName,
                           Function<T, String> codeBuild, Function<String, Optional<T>> pullData) {
        Assert.nonNull(repository);
        Assert.isStrNotBlank(serviceName);
        Assert.nonNull(codeBuild);
        Assert.nonNull(pullData);

        this.serviceName = serviceName;
        this.repository = repository;
        this.cacheAside = RedisCacheAside.<T>builder()
                .serviceName(serviceName)
                .type(this.repository.getEntityClass())
                .codeBuild(codeBuild)
                .pullData(pullData)
                .build();
    }

    /**
     * 全量刷新
     * 有些场景如果需要开机就把数据加载进redis的时候可以调用
     * 谨慎使用，该方法会一次性加载全部数据，如果数据量很大请自行实现
     *
     * @param compel 是否强制刷新
     */
    public void refresh(boolean compel) {
        String existKey = RedisUtils.support().format(String.format(EXIST_KEY_FM, serviceName));
        if (!compel && RedisUtils.support().isExist(existKey)) return;

        DistributedLock distributedLock = SpringContextUtils.getBean(DistributedLock.class);
        String lockKey = RedisUtils.support().format(String.format(LOCK_KEY_FM, serviceName));
        distributedLock.tryLock(lockKey, 1000, () -> {
            var data = repository.list();
            cacheAside.clear();
            if (ObjectUtils.nonNull(data)) cacheAside.refresh(data);
            RedisUtils.support().getOps4str().set(existKey, true);

            return null;
        });
    }

    /**
     * 获取缓存
     */
    public T get(String code) {
        return cacheAside.get(code).orElse(null);
    }

    /**
     * 批量查询指定缓存
     */
    public List<T> list(Collection<String> codes) {
        return cacheAside.list(codes);
    }

    /**
     * 刷新指定缓存
     */
    public void refresh(String code) {
        cacheAside.del(code);
        cacheAside.get(code);
    }

}
