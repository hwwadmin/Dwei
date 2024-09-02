package com.dwei.domain.repository;

import com.dwei.core.mvc.repository.IBaseRepository;
import com.dwei.domain.entity.DictEntity;
import com.dwei.domain.mapper.DictMapper;
import com.dwei.domain.query.dict.DictQuery;

import java.util.List;

public interface IDictRepository extends IBaseRepository<DictMapper, DictEntity> {

    List<DictEntity> query(DictQuery query);

    /** 字典code是否存在 */
    boolean existsByCode(String code);

}
