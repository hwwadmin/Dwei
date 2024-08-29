package com.dwei.core.mvc.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dwei.common.utils.Lists;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.mvc.pojo.entity.BaseEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class BaseRepositoryImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T>
        implements IBaseRepository<M, T> {

    public List<T> listByIds(final Collection<? extends Serializable> idList) {
        if (ObjectUtils.isNull(idList)) return Lists.of();
        return getBaseMapper().selectBatchIds(idList);
    }

    @Override
    public M getMapper() {
        return baseMapper;
    }

}
