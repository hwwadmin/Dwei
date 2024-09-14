package com.dwei.core.mvc.repository;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.override.MybatisMapperProxy;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.reflect.GenericTypeUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.dwei.common.utils.Assert;
import com.dwei.common.utils.Lists;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.mvc.condition.QueryConditionUtils;
import com.dwei.core.mvc.page.PageUtils;
import com.dwei.core.mvc.pojo.entity.BaseEntity;
import com.dwei.core.utils.SpringContextUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("unchecked, unused, rawtypes, deprecation")
public abstract class BaseRepository<M extends BaseMapper<T>, T extends BaseEntity> implements IBaseRepository<M, T> {

    // 默认批次提交数量
    protected static final int DEFAULT_BATCH_SIZE = 1000;

    private M baseMapper;

    protected final Class<?>[] typeArguments = GenericTypeUtils.resolveTypeArguments(getClass(), BaseRepository.class);

    protected final Class<M> mapperClass = currentMapperClass();
    protected final Class<T> entityClass = currentModelClass();

    private volatile SqlSessionFactory sqlSessionFactory;
    protected final Log log = LogFactory.getLog(getClass());

    protected Class<M> currentMapperClass() {
        return (Class<M>) this.typeArguments[0];
    }

    protected Class<T> currentModelClass() {
        return (Class<T>) this.typeArguments[1];
    }

    protected SqlSessionFactory getSqlSessionFactory() {
        if (this.sqlSessionFactory == null) {
            synchronized (this) {
                if (this.sqlSessionFactory == null) {
                    Object target = getMapper();
                    // 这个检查目前看着来说基本上可以不用判断Aop是不是存在了.
                    if (com.baomidou.mybatisplus.extension.toolkit.AopUtils.isLoadSpringAop()) {
                        while (AopUtils.isAopProxy(target)) {
                            target = AopProxyUtils.getSingletonTarget(target);
                        }
                    }
                    if (target instanceof MybatisMapperProxy) {
                        MybatisMapperProxy mybatisMapperProxy = (MybatisMapperProxy) Proxy.getInvocationHandler(target);
                        SqlSessionTemplate sqlSessionTemplate = (SqlSessionTemplate) mybatisMapperProxy.getSqlSession();
                        this.sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
                    } else {
                        this.sqlSessionFactory = GlobalConfigUtils.currentSessionFactory(this.entityClass);
                    }
                }
            }
        }
        return this.sqlSessionFactory;
    }

    protected String getSqlStatement(SqlMethod sqlMethod) {
        return SqlHelper.getSqlStatement(mapperClass, sqlMethod);
    }

    @Override
    public M getMapper() {
        if (ObjectUtils.nonNull(baseMapper)) return baseMapper;
        synchronized (this) {
            if (ObjectUtils.nonNull(baseMapper)) return baseMapper;
            baseMapper = SpringContextUtils.getBean(mapperClass);
        }
        return baseMapper;
    }

    @Override
    public Class<T> getEntityClass() {
        return entityClass;
    }

    @Override
    public QueryChainWrapper<T> query() {
        return ChainWrappers.queryChain(getMapper());
    }

    @Override
    public LambdaQueryChainWrapper<T> lambdaQuery() {
        return ChainWrappers.lambdaQueryChain(getMapper(), getEntityClass());
    }

    @Override
    public T getById(Serializable id) {
        return getMapper().selectById(id);
    }

    @Override
    public Optional<T> getOptById(Serializable id) {
        return Optional.ofNullable(getById(id));
    }

    @Override
    public List<T> listByIds(final Collection<? extends Serializable> idList) {
        if (ObjectUtils.isNull(idList)) return Lists.of();
        return getMapper().selectBatchIds(idList);
    }

    @Override
    public List<T> list() {
        return getMapper().selectList(Wrappers.emptyWrapper());
    }

    @Override
    public List<T> autoQuery(Object condition) {
        return QueryConditionUtils.chainBuild(getMapper(), condition).list();
    }

    @Override
    public List<T> autoPage(Object condition) {
        PageUtils.startPage();
        return autoQuery(condition);
    }

    @Override
    public void save(T entity) {
        Assert.nonNull(entity);
        entity.init();
        boolean result = Objects.isNull(getById(entity.getId())) ?
                SqlHelper.retBool(getMapper().insert(entity)) :
                SqlHelper.retBool(getMapper().updateById(entity));
        Assert.isTrue(result, "保存失败");
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    @Override
    public void saveBatch(List<T> entityList) {
        saveBatch(entityList, DEFAULT_BATCH_SIZE);
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    @Override
    public void saveBatch(List<T> entityList, int batchSize) {
        if (ObjectUtils.isNull(entityList)) return;
        var data = entityList.stream()
                .filter(ObjectUtils::nonNull)
                .peek(T::init)
                .toList();
        if (ObjectUtils.isNull(data)) return;

        boolean result = SqlHelper.saveOrUpdateBatch(getSqlSessionFactory(), this.mapperClass, this.log, entityList, batchSize, (sqlSession, entity) -> {
            return CollectionUtils.isEmpty(sqlSession.selectList(getSqlStatement(SqlMethod.SELECT_BY_ID), entity));
        }, (sqlSession, entity) -> {
            MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
            param.put(Constants.ENTITY, entity);
            sqlSession.update(getSqlStatement(SqlMethod.UPDATE_BY_ID), param);
        });
        Assert.isTrue(result, "批量保存失败");
    }

    @Override
    public void del(T entity) {
        var id = entity.getId();
        Assert.nonNull(id);
        getMapper().deleteById(id);
    }

    @Override
    public void softDel(T entity) {
        entity.del();
        save(entity);
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    @Override
    public void delBatch(List<T> entityList) {
        if (ObjectUtils.isNull(entityList)) return;
        var ids = entityList.stream()
                .map(T::getId)
                .filter(ObjectUtils::nonNull)
                .toList();
        if (ObjectUtils.isNull(ids)) return;
        getMapper().deleteBatchIds(ids);
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    @Override
    public void softDelBatch(List<T> entityList) {
        entityList.forEach(T::del);
        saveBatch(entityList);
    }

}
