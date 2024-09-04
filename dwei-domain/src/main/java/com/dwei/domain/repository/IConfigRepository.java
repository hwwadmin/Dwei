package com.dwei.domain.repository;

import com.dwei.core.mvc.repository.IBaseRepository;
import com.dwei.domain.entity.ConfigEntity;
import com.dwei.domain.mapper.ConfigMapper;

public interface IConfigRepository extends IBaseRepository<ConfigMapper, ConfigEntity> {

    ConfigEntity findByKey(String key);

}
