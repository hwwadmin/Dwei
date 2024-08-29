package com.dwei.core.guide;

import cn.hutool.core.annotation.AnnotationUtil;
import com.dwei.core.guide.listener.ApplicationFailedEventListener;
import com.dwei.core.guide.listener.ContextClosedEventListener;
import com.dwei.core.utils.ScannerUtils;
import com.google.common.collect.Lists;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;

import java.util.Arrays;
import java.util.Collection;

/**
 * 系统启动引导
 *
 * @author hww
 */
public abstract class ApplicationBoot {

    /**
     * 创建spring对象
     */
    public static SpringApplication boot(Class<?>... primarySources) {
        SpringApplication springApplication = new SpringApplication(primarySources);
        // 避免LINUX下文件名乱码
        System.setProperty("sun.jnu.encoding", "utf-8");
        // 不允许命令行设置属性
        springApplication.setAddCommandLineProperties(false);
        // 添加事件监听器
        Collection<ApplicationListener<?>> listeners = Lists.newArrayList();
        listeners.add(new ContextClosedEventListener());
        listeners.add(new ApplicationFailedEventListener());
        ApplicationListener<?>[] applicationListeners = new ApplicationListener[listeners.size()];
        listeners.toArray(applicationListeners);
        springApplication.addListeners(applicationListeners);

        // 添加springboot对应的扫包范围到注解扫描工具类
        Arrays.stream(primarySources).forEach(source -> {
            SpringBootApplication springBootApplication = AnnotationUtil.getAnnotation(source, SpringBootApplication.class);
            ScannerUtils.addPackages(Arrays.asList(springBootApplication.scanBasePackages()));
        });

        return springApplication;
    }

}
