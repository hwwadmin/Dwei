package com.dwei.domain.repository.impl;

import com.dwei.core.mvc.repository.BaseRepositoryImpl;
import com.dwei.domain.entity.ConfigEntity;
import com.dwei.domain.repository.IConfigRepository;
import com.dwei.domain.mapper.ConfigMapper;
import org.springframework.stereotype.Service;

@Service
public class ConfigRepositoryImpl extends BaseRepositoryImpl<ConfigMapper, ConfigEntity>
        implements IConfigRepository {

}




