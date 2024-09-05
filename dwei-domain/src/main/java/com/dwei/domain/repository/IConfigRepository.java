package com.dwei.domain.repository;

import com.dwei.core.mvc.repository.IBaseRepository;
import com.dwei.domain.entity.ConfigEntity;
import com.dwei.domain.mapper.ConfigMapper;

public interface IConfigRepository extends IBaseRepository<ConfigMapper, ConfigEntity> {

    /** 根据参数键查询参数配置 */
    ConfigEntity findByKey(String key);

}
