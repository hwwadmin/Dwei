package com.dwei.domain.repository.impl;

import com.dwei.common.utils.Assert;
import com.dwei.common.utils.Lists;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.mvc.repository.BaseRepository;
import com.dwei.domain.entity.DictDataEntity;
import com.dwei.domain.mapper.DictDataMapper;
import com.dwei.domain.repository.IDictDataRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class DictDataRepository extends BaseRepository<DictDataMapper, DictDataEntity>
        implements IDictDataRepository {

    @Override
    public List<DictDataEntity> findByDictCode(String dictCode) {
        Assert.nonNull(dictCode);
        return lambdaQuery()
                .eq(DictDataEntity::getDictCode, dictCode)
                .list();
    }

    @Override
    public List<DictDataEntity> findByDictCode(Collection<String> dictCode) {
        if (ObjectUtils.isNull(dictCode)) return Lists.of();
        return lambdaQuery()
                .in(DictDataEntity::getDictCode, dictCode)
                .list();
    }

}