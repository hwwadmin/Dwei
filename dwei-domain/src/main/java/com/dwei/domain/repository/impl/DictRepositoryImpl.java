package com.dwei.domain.repository.impl;

import com.dwei.common.utils.Assert;
import com.dwei.core.mvc.repository.BaseRepositoryImpl;
import com.dwei.domain.entity.DictEntity;
import com.dwei.domain.repository.IDictRepository;
import com.dwei.domain.mapper.DictMapper;
import org.springframework.stereotype.Service;

@Service
public class DictRepositoryImpl extends BaseRepositoryImpl<DictMapper, DictEntity>
        implements IDictRepository {

    @Override
    public DictEntity findByCode(String code) {
        return lambdaQuery().eq(DictEntity::getCode, code).one();
    }

    @Override
    public boolean existsByCode(String code) {
        Assert.isStrNotBlank(code);
        return lambdaQuery().eq(DictEntity::getCode, code.trim()).exists();
    }

}




