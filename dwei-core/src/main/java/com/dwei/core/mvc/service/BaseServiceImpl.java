package com.dwei.core.mvc.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dwei.common.utils.Lists;
import com.dwei.common.utils.ObjectUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IBaseService<M, T> {

    public List<T> listByIds(final Collection<? extends Serializable> idList) {
        if (ObjectUtils.isNull(idList)) return Lists.of();
        return getBaseMapper().selectBatchIds(idList);
    }

    @Override
    public M getMapper() {
        return baseMapper;
    }

}
