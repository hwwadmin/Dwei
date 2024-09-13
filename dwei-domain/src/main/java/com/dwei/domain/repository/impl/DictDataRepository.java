package com.dwei.domain.repository.impl;

import com.dwei.core.mvc.repository.BaseRepository;
import com.dwei.domain.entity.DictDataEntity;
import com.dwei.domain.mapper.DictDataMapper;
import com.dwei.domain.repository.IDictDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictDataRepository extends BaseRepository<DictDataMapper, DictDataEntity>
        implements IDictDataRepository {

    @Override
    public List<DictDataEntity> findByDictCode(String dictCode) {
        return lambdaQuery()
                .eq(DictDataEntity::getDictCode, dictCode)
                .list();
    }

}




