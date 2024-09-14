package com.dwei.core.mvc.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.dwei.core.mvc.pojo.entity.BaseEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IBaseRepository<M extends BaseMapper<T>, T extends BaseEntity> {

    /**
     * 获取明确类型的mapper对象
     */
    M getMapper();

    /**
     * 获取 entity 的 class
     */
    Class<T> getEntityClass();

    /**
     * 链式查询 普通
     */
    QueryChainWrapper<T> query();

    /**
     * 链式查询 lambda 式
     */
    LambdaQueryChainWrapper<T> lambdaQuery();

    /**
     * 根据 ID 查询
     * 对应数据不存在返回null
     */
    T get(Serializable id);

    /**
     * 根据 ID 查询
     * 对应数据不存在会抛出异常
     */
    T getEx(Serializable id);

    /**
     * 根据 ID 查询，返回一个Option对象
     */
    Optional<T> getOpt(Serializable id);

    /**
     * 根据ID 批量查询
     */
    List<T> listByIds(Collection<? extends Serializable> idList);

    /**
     * 获取全部数据
     */
    List<T> list();

    /**
     * 自动条件查询
     * 条件由解析condition后自动构造
     */
    List<T> autoQuery(Object condition);

    /**
     * 自动分页查询
     * 条件由解析condition后自动构造
     */
    List<T> autoPage(Object condition);

    /**
     * 保存（新增or更新）
     */
    void save(T entity);

    /**
     * 批量保存
     */
    void saveBatch(List<T> entityList);

    /**
     * 批量保存
     */
    void saveBatch(List<T> entityList, int batchSize);

    /**
     * 硬删除
     */
    void del(T entity);

    /**
     * 软删除
     */
    void softDel(T entity);

    /**
     * 批量硬删除
     */
    void delBatch(List<T> entityList);

    /**
     * 批量软删除
     */
    void softDelBatch(List<T> entityList);

}
