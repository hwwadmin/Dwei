package com.dwei.core.mvc.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dwei.common.utils.Assert;
import com.dwei.common.utils.Lists;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.mvc.condition.QueryConditionUtils;
import com.dwei.core.mvc.page.PageUtils;
import com.dwei.core.mvc.pojo.entity.BaseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class BaseRepository<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T>
        implements IBaseRepository<M, T> {

    public List<T> listByIds(final Collection<? extends Serializable> idList) {
        if (ObjectUtils.isNull(idList)) return Lists.of();
        return getBaseMapper().selectBatchIds(idList);
    }

    @Override
    public M getMapper() {
        return baseMapper;
    }

    @Override
    public List<T> autoQueue(Object condition) {
        return QueryConditionUtils.chainBuild(getBaseMapper(), condition).list();
    }

    @Override
    public List<T> autoPage(Object condition) {
        PageUtils.startPage();
        return autoQueue(condition);
    }

    @Override
    public void del(T entity) {
        var id = entity.getId();
        Assert.nonNull(id);
        getMapper().deleteById(id);
    }

    @Override
    public void softDel(T entity) {
        entity.del();
        updateById(entity);
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    @Override
    public void delBatch(List<T> entityList) {
        if (ObjectUtils.isNull(entityList)) return;
        var ids = entityList.stream()
                .map(T::getId)
                .filter(ObjectUtils::nonNull)
                .toList();
        if (ObjectUtils.isNull(ids)) return;
        getMapper().deleteBatchIds(ids);
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    @Override
    public void softDelBatch(List<T> entityList) {
        entityList.forEach(T::del);
        updateBatchById(entityList);
    }

}
