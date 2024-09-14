package com.dwei.core.mvc.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dwei.common.utils.Assert;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.lock.DistributedLock;
import com.dwei.core.mvc.pojo.entity.BaseEntity;
import com.dwei.core.redis.RedisCacheAside;
import com.dwei.core.utils.RedisUtils;
import com.dwei.core.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 抽象的缓存资源仓库 Repository
 * 基于{@link RedisCacheAside}旁路缓存策略自动构造缓存资源仓库，简化使用
 *
 * @author hww
 */
@Slf4j
public abstract class CacheRepository<
        R extends IBaseRepository<M, T>,
        M extends BaseMapper<T>,
        T extends BaseEntity,
        C> {

    protected static final String LOCK_KEY_FM = "cr:%s:refresh:lock";
    protected static final String EXIST_KEY_FM = "cr:%s:refresh:exist";

    protected final R repository;
    protected final RedisCacheAside<C> cacheAside;

    /** 服务名称 */
    protected final String serviceName;

    public CacheRepository(R repository, String serviceName, Class<C> clazz,
                           Function<C, String> codeBuild, BiFunction<String, R, Optional<C>> pullData) {
        Assert.nonNull(repository);
        Assert.isStrNotBlank(serviceName);
        Assert.nonNull(codeBuild);
        Assert.nonNull(pullData);

        this.serviceName = serviceName;
        this.repository = repository;
        this.cacheAside = RedisCacheAside.<C>builder()
                .serviceName(serviceName)
                .type(clazz)
                .codeBuild(codeBuild)
                .pullData(code -> pullData.apply(code, repository))
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
            log.info("[{}]缓存全量刷新", serviceName);
            var data = repository.findAll();
            cacheAside.clear();
            if (ObjectUtils.nonNull(data)) cacheAside.refresh(getAllDate());
            RedisUtils.support().getOps4str().set(existKey, true);

            return null;
        });
    }

    /**
     * 获取全部数据
     */
    protected abstract List<C> getAllDate();

    /**
     * 获取缓存
     */
    public C get(String code) {
        return cacheAside.get(code).orElse(null);
    }

    /**
     * 批量查询指定缓存
     */
    public List<C> list(Collection<String> codes) {
        return cacheAside.list(codes);
    }

    /**
     * 刷新指定缓存
     */
    public void refresh(String code) {
        log.info("[{}]刷新指定缓存 - code:[{}]", serviceName, code);
        cacheAside.del(code);
        cacheAside.get(code);
    }

}
