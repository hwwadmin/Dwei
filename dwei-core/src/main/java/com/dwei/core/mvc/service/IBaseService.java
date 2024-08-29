package com.dwei.core.mvc.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dwei.core.mvc.pojo.entity.BaseEntity;

public interface IBaseService<M extends BaseMapper<T>, T extends BaseEntity> extends IService<T> {

    /**
     * 获取明确类型的mapper对象
     */
    M getMapper();

}
