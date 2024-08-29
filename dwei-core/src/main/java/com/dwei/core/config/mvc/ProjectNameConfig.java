package com.dwei.core.config.mvc;

import com.dwei.common.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 项目名配置
 * 注入环境变量中
 *
 * @author hww
 */
@Configuration
@Slf4j
public class ProjectNameConfig implements EnvironmentAware {

    @Value("${spring.application.name:#{''}}")
    private String applicationName;

    @Override
    public void setEnvironment(Environment environment) {
        if (ObjectUtils.nonNull(System.getProperty("project.name"))) return;
        log.info("项目名:[{}]", applicationName);
        System.setProperty("project.name", applicationName);
    }

}
