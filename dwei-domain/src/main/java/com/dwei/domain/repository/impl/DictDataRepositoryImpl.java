package com.dwei.domain.repository.impl;

import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.mvc.repository.BaseRepositoryImpl;
import com.dwei.domain.entity.DictDataEntity;
import com.dwei.domain.query.dictdata.DictDataQuery;
import com.dwei.domain.repository.IDictDataRepository;
import com.dwei.domain.mapper.DictDataMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictDataRepositoryImpl extends BaseRepositoryImpl<DictDataMapper, DictDataEntity>
        implements IDictDataRepository {

    @Override
    public List<DictDataEntity> query(DictDataQuery query) {
        return lambdaQuery()
                .eq(ObjectUtils.nonNull(query.getDictCode()), DictDataEntity::getDictCode, query.getDictCode())
                .in(ObjectUtils.nonNull(query.getDictCodeIn()), DictDataEntity::getDictCode, query.getDictCodeIn())
                .list();
    }

}




