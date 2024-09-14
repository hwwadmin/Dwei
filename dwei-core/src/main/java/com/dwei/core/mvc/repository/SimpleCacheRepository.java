package com.dwei.core.mvc.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dwei.core.mvc.pojo.entity.BaseEntity;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 单一缓存资源仓库 Repository
 * 数据对象和缓存对象是同一个T
 *
 * @author hww
 */
public class SimpleCacheRepository<
        R extends IBaseRepository<M, T>,
        M extends BaseMapper<T>,
        T extends BaseEntity>
        extends CacheRepository<R, M, T, T> {

    public SimpleCacheRepository(R repository, String serviceName) {
        // 默认使用id做code的情况
        super(repository, serviceName, repository.getEntityClass(),
                t -> String.valueOf(t.getId()), (code, r) -> r.getOpt(Long.valueOf(code)));
    }

    public SimpleCacheRepository(R repository, String serviceName,
                                 Function<T, String> codeBuild, BiFunction<String, R, Optional<T>> pullData) {
        super(repository, serviceName, repository.getEntityClass(), codeBuild, pullData);
    }

    @Override
    protected List<T> getAllDate() {
        return repository.findAll();
    }

}
