package com.dwei.core.mvc.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IBaseService<M extends BaseMapper<T>, T> extends IService<T> {

    /**
     * 获取明确类型的mapper对象
     */
    M getMapper();

}
