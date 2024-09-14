package com.dwei.domain.repository;

import com.dwei.core.mvc.repository.IBaseRepository;
import com.dwei.domain.entity.DictDataEntity;
import com.dwei.domain.mapper.DictDataMapper;

import java.util.Collection;
import java.util.List;

public interface IDictDataRepository extends IBaseRepository<DictDataMapper, DictDataEntity> {

    /** 根据字典类型dictCode查询 */
    List<DictDataEntity> findByDictCode(String dictCode);
    List<DictDataEntity> findByDictCode(Collection<String> dictCode);

}
