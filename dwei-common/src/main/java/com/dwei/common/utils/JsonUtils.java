package com.dwei.common.utils;

import com.dwei.common.constants.AppConstants;
import com.dwei.common.enums.DateFormatEnum;
import com.dwei.common.exception.UtilsException;
import com.dwei.common.jackson.deserializer.Int2BooleanDeserializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * JSON(jackson)工具类
 *
 * @author hww
 */
public abstract class JsonUtils {

    private static final ObjectMapper objectMapper = createObjectMapper();
    private static final ObjectMapper originObjectMapper = createObjectMapper(false);

    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw UtilsException.exception("json 解析失败");
        }
    }

    public static String toJsonOrigin(Object object) {
        try {
            return originObjectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw UtilsException.exception("json 解析失败");
        }
    }

    public static <T> T format(String json, TypeReference<T> type) {
        return format(json, objectMapper.getTypeFactory().constructType(type));
    }

    public static <T> T format(String json, Class<T> type) {
        return format(json, objectMapper.getTypeFactory().constructType(type));
    }

    public static <T> T format(String json, Type type) {
        return format(json, objectMapper.getTypeFactory().constructType(type));
    }

    public static <T> T format(String json, JavaType type) {
        if (ObjectUtils.isNull(json)) return null;
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw UtilsException.exception("Json字符串解析错误，解析" + type.getTypeName(), e);
        }
    }

    public static <T, K> T convert(K k, TypeReference<T> type) {
        String json = toJson(k);
        return format(json, type);
    }

    /**
     * Json字符串解析为List<Map<String, Object>>
     */
    public static List<Map<String, Object>> jsonStr2List(String jsonString) {
        if (ObjectUtils.isNull(jsonString)) {
            return Lists.newArrayList();
        }
        try {
            //noinspection unchecked
            return objectMapper.readValue(jsonString, ArrayList.class);
        } catch (Exception e) {
            throw UtilsException.exception("Json字符串解析为List<Map<String, Object>>错误", e);
        }
    }

    /**
     * Json字符串解析为List<T>
     */
    public static <T> List<T> jsonStr2List(String jsonString, Class<T> clazz) {
        if (ObjectUtils.isNull(jsonString)) {
            return Lists.newArrayList();
        }
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, clazz);
            return objectMapper.readValue(jsonString, javaType);
        } catch (Exception e) {
            throw UtilsException.exception("Json字符串解析为List<T>错误", e);
        }
    }

    /**
     * Json字符串解析为Map
     */
    public static Map<String, Object> jsonStr2Map(String jsonString) {
        if (ObjectUtils.isNull(jsonString)) {
            return Maps.newHashMap();
        }
        try {
            //noinspection unchecked
            return objectMapper.readValue(jsonString, Map.class);
        } catch (Exception e) {
            throw UtilsException.exception("Json字符串解析为Map错误", e);
        }
    }

    /**
     * Json字符串解析为javaBean对象
     */
    public static <T> T jsonStr2Bean(String jsonString, Class<T> clazz) {
        if (ObjectUtils.isNull(jsonString)) {
            return null;
        }
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            throw UtilsException.exception("Json字符串解析为javaBean对象错误", e);
        }
    }

    /**
     * javaBean对象解析为Json字符串
     * 包含MAP集合等
     */
    public static <T> String bean2JsonStr(T object) {
        if (Objects.isNull(object)) {
            return AppConstants.MARK_EMPTY_JSONOBJECT;
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw UtilsException.exception("javaBean对象解析为Json字符串错误", e);
        }
    }

    /**
     * List<T>解析为Json字符串
     */
    public static <T> String list2JsonStr(List<T> objects) {
        if (ObjectUtils.isNull(objects)) return AppConstants.MARK_EMPTY_JSONARRAY;
        try {
            return objectMapper.writeValueAsString(objects);
        } catch (Exception e) {
            throw UtilsException.exception("List<T>解析为Json字符串错误", e);
        }
    }

    /**
     * MAP集合转javaBean对象
     */
    public static <T> T map2bean(Map<String, Object> map, Class<T> clazz) {
        try {
            if (Objects.isNull(map) || map.isEmpty()) {
                return ReflectUtils.newInstance(clazz);
            }
            return jsonStr2Bean(bean2JsonStr(map), clazz);
        } catch (Exception e) {
            throw UtilsException.exception("MAP集合转javaBean对象异常", e);
        }
    }

    /**
     * javaBean对象转MAP集合
     */
    public static Map<String, Object> bean2map(Object bean) {
        try {
            return jsonStr2Map(bean2JsonStr(bean));
        } catch (Exception e) {
            throw UtilsException.exception("bean转换map集合对象异常", e);
        }
    }

    public static JavaType constructParametricType(Class<?> parametrized, Type type) {
        JavaType javaType = objectMapper.getTypeFactory().constructType(type);
        return objectMapper.getTypeFactory().constructParametricType(parametrized, javaType);
    }

    /**
     * 获取 ObjectMapper 对象
     */
    public static ObjectMapper createObjectMapper() {
        return createObjectMapper(true);
    }

    /**
     * 获取 ObjectMapper 对象
     */
    public static ObjectMapper createObjectMapper(boolean prettier) {
        ObjectMapper objectMapper = new ObjectMapper();
        // 格式化JSON
        if (prettier) {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
        // 日期转换
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        // 设置为东八区
        objectMapper.setTimeZone(TimeZone.getTimeZone(AppConstants.GMT8));
        // 空值不序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 反序列化时，属性不存在的兼容处理
        objectMapper.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 序列化时，日期的统一格式
        objectMapper.setDateFormat(new SimpleDateFormat(DateFormatEnum.YYYYVMMVDDHHSS.getValue()));
        // 序列化枚举是以toString()来输出，默认false，即默认以name()来输出
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        // 序列化枚举是以ordinal()来输出，默认false
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, false);
        // 类为空时，不要抛异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 反序列化时,遇到未知属性时是否引起结果失败
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 单引号处理
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // JSR310
        objectMapper.registerModule(new JavaTimeModule());
        // Jdk8
        objectMapper.registerModule(new Jdk8Module());
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Boolean.class, new Int2BooleanDeserializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }

}
