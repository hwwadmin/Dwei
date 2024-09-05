package com.dwei.core.mvc.condition.info;

import com.dwei.common.utils.*;
import com.dwei.core.mvc.condition.annotation.QueryCondition;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 查询条件信息管理器
 *
 * @author hww
 */
public class QueryInfoManager {

    private static final Map<Class<?>, List<QueryInfo>> cache = Maps.of();

    private static class QueryInfoManagerHolder {
        private static final QueryInfoManager instance = new QueryInfoManager();
    }

    public static QueryInfoManager getInstance() {
        return QueryInfoManagerHolder.instance;
    }

    private QueryInfoManager() {

    }

    /**
     * 获取查询条件信息
     */
    public List<QueryInfo> getQueryInfo(Object condition) {
        Class<?> clazz = condition.getClass();
        if (!cache.containsKey(clazz)) analyse(condition);
        return cache.get(clazz);
    }

    /**
     * 查询条件解析
     */
    private void analyse(Object condition) {
        Class<?> clazz = condition.getClass();
        synchronized (clazz) {
            if (cache.containsKey(clazz)) return;

            Field[] fields = FieldUtils.getAllFields(clazz);
            if (Objects.isNull(fields)) return;
            if (fields.length == 0) return;

            List<QueryInfo> queryInfos = Lists.of();
            Arrays.stream(fields).forEach(field -> {
                QueryCondition queryCondition = field.getAnnotation(QueryCondition.class);
                if (Objects.isNull(queryCondition)) return;

                var queryInfo = parseCondition(clazz, field, queryCondition);
                queryInfos.add(queryInfo);
            });

            cache.put(clazz, queryInfos);
        }
    }

    private QueryInfo parseCondition(Class<?> clazz, Field field, QueryCondition condition) {
        // 不设置的话默认为当前注解的属性名
        var name = ObjectUtils.nonNull(condition.name()) ? condition.name():
                field.getName();

        var queryInfo = QueryInfo.builder()
                .clazz(clazz)
                .field(field)
                .type(condition.type())
                .name(name)
                .build();

        // between操作进行额外解析
        if (ObjectUtils.equals(condition.type(), QueryType.BETWEEN)) {
            Assert.isStrNotBlank(condition.betweenName(), "between操作的关联属性未设置");
            queryInfo.setBetweenName(condition.betweenName());
            var betweenField = ReflectUtils.getField(clazz, condition.betweenName());
            Assert.nonNull(betweenField, "between关联属性名无法获取对应字段");
            queryInfo.setBetweenField(betweenField);
        }

        return queryInfo;
    }

}
