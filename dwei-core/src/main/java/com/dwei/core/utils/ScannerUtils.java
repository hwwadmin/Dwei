package com.dwei.core.utils;

import com.dwei.common.exception.UtilsException;
import com.dwei.common.utils.Assert;
import com.dwei.common.utils.Lists;
import com.dwei.common.utils.ObjectUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.util.List;

/**
 * 注解扫描工具类
 *
 * @author hww
 */
public abstract class ScannerUtils {

    // 默认包范围
    public static final List<String> basePackages = Lists.of();

    public static void addPackages(List<String> packages) {
        basePackages.addAll(packages);
    }

    public static List<Class<?>> scanAnnotationInType(Class<?> annotation) {
        return scanAnnotationInType(basePackages, annotation);
    }

    public static List<Class<?>> scanAnnotationInType(List<String> basePackages, Class<?> annotation) {
        if (ObjectUtils.isNull(basePackages)) {
            return Lists.of();
        }
        List<Class<?>> classList = Lists.of();
        for (String basePackage : basePackages) {
            classList.addAll(scanAnnotationInType(basePackage, annotation));
        }
        return classList;
    }

    /**
     * 在指定包及其子包下面查找指定注解类
     */
    public static List<Class<?>> scanAnnotationInType(String basePackage, Class<?> annotation) {
        Assert.isTrue(ObjectUtils.nonNull(basePackage), "要扫描的包路径不能为空");
        Assert.nonNull(annotation, "要扫描的注解不能为空");
        Assert.isTrue(annotation.isAnnotation(), "要扫描的对象必须是类注解");
        try {
            String packageSearchPath = String.format("classpath*:%s/**/*.class", ClassUtils.convertClassNameToResourcePath(basePackage));
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(packageSearchPath);
            List<Class<?>> classList = Lists.of();
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader metadataReader = new SimpleMetadataReaderFactory(ClassUtils.getDefaultClassLoader()).getMetadataReader(resource);
                    if (metadataReader.getAnnotationMetadata().hasAnnotation(annotation.getName())) {
                        classList.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
                    }
                }
            }
            return classList;
        } catch (Exception e) {
            throw UtilsException.exception(String.format("在包[%s]下扫描注解[%s]失败", basePackage, annotation.getName()), e);
        }
    }

}
