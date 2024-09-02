package com.dwei.domain.repository;

import com.dwei.core.mvc.repository.IBaseRepository;
import com.dwei.domain.entity.DictDataEntity;
import com.dwei.domain.mapper.DictDataMapper;
import com.dwei.domain.query.dictdata.DictDataQuery;

import java.util.List;

public interface IDictDataRepository extends IBaseRepository<DictDataMapper, DictDataEntity> {

    List<DictDataEntity> query(DictDataQuery query);

}
