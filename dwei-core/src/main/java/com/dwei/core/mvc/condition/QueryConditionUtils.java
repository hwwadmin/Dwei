package com.dwei.core.mvc.condition;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.dwei.common.utils.Assert;
import com.dwei.common.utils.ReflectUtils;
import com.dwei.core.mvc.condition.info.QueryInfoManager;

/**
 * 基于mybatisPlus的条件查询自动构造器
 * 这个自动解析只支持简单的构造，复杂的请自行编写
 *
 * @author hww
 */
public abstract class QueryConditionUtils {

    /**
     * 自动解析并构造查询条件
     */
    public static <T, M extends BaseMapper<T>> Wrapper<T> build(M mapper, Object condition) {
        return chainBuild(mapper, condition);
    }

    /**
     * 自动解析并构造查询条件
     */
    public static <T, M extends BaseMapper<T>> QueryChainWrapper<T> chainBuild(M mapper, Object condition) {
        Assert.nonNull(mapper);
        Assert.nonNull(condition);

        var queryInfos = QueryInfoManager.getInstance().getQueryInfo(condition);
        Assert.isNotEmpty(queryInfos, "查无对应查询条件信息");

        QueryChainWrapper<T> wrapper = new QueryChainWrapper<>(mapper);
        queryInfos.forEach(queryInfo -> {
            Object value = ReflectUtils.getFieldValue(condition, queryInfo.getField());
            switch (queryInfo.getType()) {
                case EQ -> wrapper.eq(queryInfo.getName(), value);
                case NEQ -> wrapper.ne(queryInfo.getName(), value);
                case LIKE -> wrapper.like(queryInfo.getName(), value);
                case IN -> wrapper.in(queryInfo.getName(), value);
                case GT -> wrapper.gt(queryInfo.getName(), value);
                case GTE -> wrapper.ge(queryInfo.getName(), value);
                case LT -> wrapper.lt(queryInfo.getName(), value);
                case LTE -> wrapper.le(queryInfo.getName(), value);
                case BETWEEN -> {
                    Object betweenValue = ReflectUtils.getFieldValue(condition, queryInfo.getBetweenField());
                    wrapper.between(queryInfo.getName(), value, betweenValue);
                }
                case IS_NULL -> wrapper.isNull(queryInfo.getName());
                case IS_NOT_NULL -> wrapper.isNotNull(queryInfo.getName());
            }
        });

        return wrapper;
    }

}
