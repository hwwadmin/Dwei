package com.dwei.core.mvc.condition.info;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;

/**
 * 查询条件信息
 *
 * @author hww
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryInfo {

    /**
     * 条件对象
     */
    private Class<?> clazz;

    /**
     * 注解字段
     */
    private Field field;

    /**
     * 操作类型
     */
    private QueryType type;

    /**
     * 实体字段名参数
     */
    private String name;

    /**
     * between的后置参数字段
     */
    private Field betweenField;

    /**
     * between操作的关联属性
     */
    private String betweenName;

}
