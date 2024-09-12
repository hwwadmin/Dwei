package com.dwei.core.mvc.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dwei.common.utils.Assert;
import com.dwei.core.mvc.pojo.entity.BaseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 列表缓存资源仓库 Repository
 * 数据对象是T，缓存对象是List<T>
 *
 * @author hww
 */
public class ListCacheRepository<
        R extends IBaseRepository<M, T>,
        M extends BaseMapper<T>,
        T extends BaseEntity>
        extends CacheRepository<R, M, T, List<T>> {

    private final Function<T, String> codeBuildFun;

    public ListCacheRepository(R repository, String serviceName,
                               Function<T, String> codeBuild,
                               BiFunction<String, R, Optional<List<T>>> pullData) {
        //noinspection unchecked
        super(repository, serviceName, (Class<List<T>>) (Class<?>) List.class,
                list -> {
                    Assert.isNotEmpty(list);
                    return codeBuild.apply(list.get(0));
                },
                pullData);
        Assert.nonNull(codeBuild);
        this.codeBuildFun = codeBuild;
    }

    @Override
    protected List<List<T>> getAllDate() {
        List<T> all = this.repository.list();
        Map<String, List<T>> group = all.stream().collect(Collectors.groupingBy(codeBuildFun));
        return group.keySet()
                .stream()
                .map(group::get)
                .collect(Collectors.toList());
    }

}
