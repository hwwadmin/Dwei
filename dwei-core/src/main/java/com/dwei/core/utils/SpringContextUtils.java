package com.dwei.core.utils;

import com.dwei.common.exception.UtilsException;
import com.dwei.common.utils.ObjectUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

/**
 * IOC帮助类
 * 依托SPRING实现基础性的IOC功能
 *
 * @author hww
 */
@SuppressWarnings("unused")
@Component
@Slf4j
public class SpringContextUtils implements ApplicationContextAware {

    /**
     * spring上下文
     */
    private static ApplicationContext applicationContext;
    private static Boolean isDev;

    /**
     * 根据BEAN的名称获取BEAN
     *
     * @param name bean的名称
     * @return 如果bean不存在会类型不正确，会抛出异常
     */
    public static <T> T getBean(String name) {
        if (!containsBean(name)) {
            throw UtilsException.exception(String.format("找不到名称为[%s]的IOC对象定义", name));
        }
        try {
            //noinspection unchecked
            return (T) applicationContext.getBean(name);
        } catch (Exception e) {
            throw UtilsException.exception(String.format("名称为[%s]的IOC对象转换错误", name), e);
        }
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    public static <T> T getBeanNoEx(Class<T> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取SPRING的全局配置文件值
     * 如果key为空或key不存在，那么直接返回null
     */
    public static String getConfig(String key) {
        verifyApplicationContext();
        if (ObjectUtils.isNull(key)) {
            return null;
        }
        return applicationContext.getEnvironment().getProperty(key);
    }

    /**
     * 根据关键字获取配置
     * 如果key为空或key不存在，那么直接返回指定的默认值
     */
    public static String getConfig(String key, String defaultValue) {
        verifyApplicationContext();
        if (ObjectUtils.isNull(key)) {
            return defaultValue;
        }
        if (!applicationContext.getEnvironment().containsProperty(key)) {
            return defaultValue;
        }
        try {
            return applicationContext.getEnvironment().getProperty(key);
        } catch (Throwable e) {
            return defaultValue;
        }
    }

    /**
     * 判断是否存在指定名称的BEAN
     */
    public static boolean containsBean(String name) {
        verifyApplicationContext();
        if (ObjectUtils.isNull(name)) {
            throw UtilsException.exception("需要获取的对象名称不能为空");
        }
        return applicationContext.containsBean(name);
    }

    /**
     * 校验上下文是否初始化
     */
    private static void verifyApplicationContext() {
        if (Objects.isNull(applicationContext)) throw UtilsException.exception("上下文未初始化");
    }

    public static ApplicationContext getApplicationContext() {
        verifyApplicationContext();
        return applicationContext;
    }

    public static boolean isDev() {
        if (isDev != null) return isDev;
        var activeProfiles = applicationContext.getEnvironment().getActiveProfiles();
        isDev = Arrays.stream(activeProfiles)
                .filter(Objects::nonNull)
                .noneMatch(profile ->
                        profile.equals("prod")
                                || profile.equals("production")
                                || profile.equals("test")
                                || profile.equals("test-local")
                );
        return isDev;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext context) {
        log.info("初始化spring上下文");
        applicationContext = context;
    }

}
