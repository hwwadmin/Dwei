package com.dwei.core.mvc.condition.info;

import com.dwei.common.utils.*;
import com.dwei.core.mvc.condition.annotation.QueryCondition;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.core.annotation.AnnotationUtils;

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
@Slf4j
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
            List<QueryInfo> queryInfos = Lists.of();

            Field[] fields = FieldUtils.getAllFields(clazz);
            if (ObjectUtils.isNull(fields)) {
                cache.put(clazz, queryInfos);
                return;
            }

            Arrays.stream(fields).forEach(field -> {
                QueryCondition conditionAnnotation = AnnotationUtils.getAnnotation(field, QueryCondition.class);
                if (Objects.isNull(conditionAnnotation)) return;

                var queryInfo = parseCondition(clazz, field, conditionAnnotation);
                queryInfos.add(queryInfo);
            });

            cache.put(clazz, queryInfos);
        }
    }

    private QueryInfo parseCondition(Class<?> clazz, Field field, QueryCondition conditionAnnotation) {
        // 不设置的话默认为当前注解的属性名
        var name = ObjectUtils.nonNull(conditionAnnotation.name()) ? conditionAnnotation.name():
                field.getName();

        var queryInfo = QueryInfo.builder()
                .clazz(clazz)
                .field(field)
                .type(conditionAnnotation.type())
                .name(conditionAnnotation.enableConvertName() ? StringUtils.toUnderlineCase(name) : name)
                .build();

        // between操作进行额外解析
        if (ObjectUtils.equals(conditionAnnotation.type(), QueryType.BETWEEN)) {
            Assert.isStrNotBlank(conditionAnnotation.betweenName(), "between操作的关联属性未设置");
            queryInfo.setBetweenName(conditionAnnotation.betweenName());
            var betweenField = ReflectUtils.getField(clazz, conditionAnnotation.betweenName());
            Assert.nonNull(betweenField, "between关联属性名无法获取对应字段");
            queryInfo.setBetweenField(betweenField);
        }

        return queryInfo;
    }

}
