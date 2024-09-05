package com.dwei.core.mvc.condition.annotation;

import com.dwei.core.mvc.condition.info.QueryType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 查询条件注解
 *
 * @author hww
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QueryCondition {

    /**
     * 操作类型
     */
    @AliasFor("type")
    QueryType value() default QueryType.EQ;
    @AliasFor("value")
    QueryType type() default QueryType.EQ;

    /**
     * 实体字段名
     * 不设置的话默认为当前注解的属性名
     */
    String name() default "";

    /**
     * between/not_between 操作的关联属性
     * 操作类型为between的时候必填
     * ps:注解的当前字段是前置参数，该关联为后置参数名称
     * 即 between(name, betweenName)
     */
    String betweenName() default "";

    /**
     * 是否启用自动转换实体字段名，默认启用
     * 驼峰 -> 下划线
     */
    boolean enableConvertName() default true;

}
