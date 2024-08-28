package com.dwei.common.utils;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import com.dwei.common.exception.UtilsException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

/**
 * 反射工具类
 *
 * @author hww
 */
public abstract class ReflectUtils {

    // ---------------------------- class ---------------------------- //

    public static <T> T newInstance(String clazz, Object... params) {
        Assert.isStrNotBlank(clazz);
        try {
            //noinspection unchecked
            return (T) newInstance(Class.forName(clazz), params);
        } catch (Exception e) {
            throw UtilsException.exception("ReflectUtils#newInstance", e);
        }
    }

    public static <T> T newInstance(Class<T> clazz, Object... params) {
        Assert.nonNull(clazz);
        try {
            return ReflectUtil.newInstance(clazz, params);
        } catch (Exception e) {
            throw UtilsException.exception("ReflectUtils#newInstance", e);
        }
    }

    public static Class<?> type2class(Type type) {
        Assert.nonNull(type);
        try {
            return (Class<?>) type;
        } catch (Exception e) {
            throw UtilsException.exception("ReflectUtils#toClass", e);
        }
    }

    public static boolean isSubClass(Object subObject, Class<?> parentClass) {
        return isSubClass(subObject.getClass(), parentClass);
    }

    public static boolean isSubClass(Class<?> subClass, Class<?> parentClass) {
        Assert.nonNull(subClass);
        Assert.nonNull(parentClass);
        return parentClass.isAssignableFrom(subClass);
    }

    // ---------------------------- field ---------------------------- //

    public static Field getField(Class<?> clazz, String fieldName) {
        Assert.nonNull(clazz);
        Assert.isStrNotBlank(fieldName);
        try {
            return ReflectUtil.getField(clazz, fieldName);
        } catch (Exception e) {
            throw UtilsException.exception("ReflectUtils#getField", e);
        }
    }

    public static Field[] getAllField(Class<?> clazz) {
        Assert.nonNull(clazz);
        try {
            return ReflectUtil.getFields(clazz);
        } catch (Exception e) {
            throw UtilsException.exception("ReflectUtils#getAllField", e);
        }
    }

    public static Class<?> getFieldClass(Class<?> clazz, String fieldName) {
        return getFieldClass(getField(clazz, fieldName));
    }

    public static Class<?> getFieldClass(Field field) {
        Assert.nonNull(field);
        return field.getType();
    }

    public static Object getFieldValue(Object obj, String fieldName) {
        Assert.nonNull(obj);
        Assert.isStrNotBlank(fieldName);
        return getFieldValue(obj, getField(obj.getClass(), fieldName));
    }

    public static Object getFieldValue(Object obj, Field field) {
        Assert.nonNull(obj);
        Assert.nonNull(field);
        try {
            return ReflectUtil.getFieldValue(obj, field);
        } catch (Exception e) {
            throw UtilsException.exception("ReflectUtils#getFieldValue", e);
        }
    }

    public static Object getFieldValue(Object obj, Method method, Field field) {
        Assert.nonNull(obj);
        Assert.nonNull(field);
        if (Objects.isNull(method)) getFieldValue(obj, field);
        try {
            return doMethod(obj, method);
        } catch (Exception e) {
            return getFieldValue(obj, field);
        }
    }

    public static void setFieldValue(Object obj, Field field, Object value) {
        Assert.nonNull(obj);
        Assert.nonNull(field);
        try {
            ReflectUtil.setFieldValue(obj, field, value);
        } catch (Exception e) {
            throw UtilsException.exception("ReflectUtils#setFieldValue", e);
        }
    }


    // ---------------------------- method ---------------------------- //

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        Assert.nonNull(clazz);
        Assert.isStrNotBlank(methodName);
        try {
            return ReflectUtil.getMethod(clazz, methodName, paramTypes);
        } catch (Exception e) {
            throw UtilsException.exception("ReflectUtils#getMethod", e);
        }
    }

    public static Method getMethod4FieldGet(Class<?> clazz, Field field) {
        Assert.nonNull(field);
        var methodName = "get" + StrUtil.upperFirst(field.getName());
        var method = getMethod(clazz, methodName);
        Assert.isTrue(ReflectUtil.isGetterOrSetter(method, false));
        return method;
    }

    public static Method getMethod4FieldSet(Class<?> clazz, Field field, Class<?> paramType) {
        Assert.nonNull(field);
        Assert.nonNull(paramType);
        var methodName = "set" + StrUtil.upperFirst(field.getName());
        var method = getMethod(clazz, methodName, paramType);
        Assert.isTrue(ReflectUtil.isGetterOrSetter(method, false));
        return method;
    }

    public static Object doMethod(Object obj, Method method) {
        Assert.nonNull(obj);
        Assert.nonNull(method);
        try {
            return ReflectUtil.invoke(obj, method);
        } catch (Exception e) {
            throw UtilsException.exception("ReflectUtils#doMethod", e);
        }
    }

    // ---------------------------- argument ---------------------------- //

    public static Class<?> getClassArgument(Class<?> clazz) {
        return getClassArgument(clazz, 0);
    }

    public static Class<?> getClassArgument(Class<?> clazz, int index) {
        try {
            var type = TypeUtil.getTypeArgument(clazz, index);
            return type2class(type);
        } catch (Exception e) {
            throw UtilsException.exception("ReflectUtils#getClassArgument", e);
        }
    }

    public static Class<?>[] getTypeArguments(Class<?> clazz) {
        try {
            var type = TypeUtil.getTypeArguments(clazz);
            return Arrays.stream(type).map(ReflectUtils::type2class).toList().toArray(new Class[0]);
        } catch (Exception e) {
            throw UtilsException.exception("ReflectUtils#getTypeArguments", e);
        }
    }

}
