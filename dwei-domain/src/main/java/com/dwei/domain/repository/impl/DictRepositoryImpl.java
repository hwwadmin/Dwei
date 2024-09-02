package com.dwei.domain.repository.impl;

import com.dwei.common.utils.Assert;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.mvc.repository.BaseRepositoryImpl;
import com.dwei.domain.entity.DictEntity;
import com.dwei.domain.query.dict.DictQuery;
import com.dwei.domain.repository.IDictRepository;
import com.dwei.domain.mapper.DictMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictRepositoryImpl extends BaseRepositoryImpl<DictMapper, DictEntity>
        implements IDictRepository {

    @Override
    public List<DictEntity> query(DictQuery query) {
        return lambdaQuery()
                .eq(ObjectUtils.nonNull(query.getName()), DictEntity::getName, query.getName())
                .eq(ObjectUtils.nonNull(query.getCode()), DictEntity::getCode, query.getCode())
                .list();
    }

    @Override
    public boolean existsByCode(String code) {
        Assert.isStrNotBlank(code);
        return lambdaQuery().eq(DictEntity::getCode, code.trim()).exists();
    }

}




