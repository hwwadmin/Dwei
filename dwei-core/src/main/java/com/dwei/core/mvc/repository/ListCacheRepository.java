package com.dwei.core.mvc.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dwei.common.utils.Assert;
import com.dwei.common.utils.JsonUtils;
import com.dwei.common.utils.Lists;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.mvc.pojo.entity.BaseEntity;

import java.util.Collection;
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
                    return codeBuild.apply(convert(list.get(0), repository.getEntityClass()));
                },
                pullData);
        Assert.nonNull(codeBuild);
        this.codeBuildFun = codeBuild;
    }

    @Override
    protected List<List<T>> getAllDate() {
        List<T> all = this.repository.findAll();
        Map<String, List<T>> group = all.stream().collect(Collectors.groupingBy(codeBuildFun));
        return group.keySet()
                .stream()
                .map(group::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<T> get(String code) {
        var data = super.get(code);
        List<T> result = Lists.of();
        if (ObjectUtils.isNull(data)) return result;
        for (Object obj : data) result.add(convert(obj, repository.getEntityClass()));
        return result;
    }

    @Override
    public List<List<T>> list(Collection<String> codes) {
        var data = super.list(codes);
        List<List<T>> result = Lists.of();
        if (ObjectUtils.isNull(data)) return result;
        for (List<?> t : data) {
            if (ObjectUtils.isNull(t)) continue;
            List<T> itemResult = Lists.of();
            for (Object obj : t) {
                itemResult.add(convert(obj, repository.getEntityClass()));
            }
            result.add(itemResult);
        }
        return result;
    }

    /**
     * 类型转换
     * 由于从缓存读出来的数据，list的泛型擦除了所以被解析成map，需要类型转换成实际需要的数据类型
     */
    protected static <T> T convert(Object obj, Class<T> clazz) {
        return JsonUtils.map2bean(JsonUtils.bean2map(obj), clazz);
    }

}
