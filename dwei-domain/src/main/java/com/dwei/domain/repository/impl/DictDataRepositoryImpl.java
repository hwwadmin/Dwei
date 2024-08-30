package com.dwei.domain.repository.impl;

import com.dwei.core.mvc.repository.BaseRepositoryImpl;
import com.dwei.domain.entity.DictDataEntity;
import com.dwei.domain.repository.IDictDataRepository;
import com.dwei.domain.mapper.DictDataMapper;
import org.springframework.stereotype.Service;

@Service
public class DictDataRepositoryImpl extends BaseRepositoryImpl<DictDataMapper, DictDataEntity>
        implements IDictDataRepository {

}




