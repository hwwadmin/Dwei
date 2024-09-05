package com.dwei.core.mvc.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dwei.core.mvc.pojo.entity.BaseEntity;

import java.util.List;

public interface IBaseRepository<M extends BaseMapper<T>, T extends BaseEntity> extends IService<T> {

    /**
     * 获取明确类型的mapper对象
     */
    M getMapper();

    /**
     * 自动条件查询
     * 条件由解析condition后自动构造
     */
    List<T> autoQueue(Object condition);

    /**
     * 自动分页查询
     * 条件由解析condition后自动构造
     */
    List<T> autoPage(Object condition);

}
