package com.dwei.domain.repository.impl;

import com.dwei.core.mvc.repository.BaseRepository;
import com.dwei.domain.entity.ConfigEntity;
import com.dwei.domain.mapper.ConfigMapper;
import com.dwei.domain.repository.IConfigRepository;
import org.springframework.stereotype.Service;

@Service
public class ConfigRepository extends BaseRepository<ConfigMapper, ConfigEntity>
        implements IConfigRepository {

    @Override
    public ConfigEntity findByKey(String key) {
        return lambdaQuery().eq(ConfigEntity::getKey, key).one();
    }

}




