package com.dwei.admin.start;

import com.dwei.core.guide.ApplicationBoot;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 系统启动类
 */
@SpringBootApplication(scanBasePackages = {
        "com.dwei.*",
}, exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableScheduling
@EnableAsync
@MapperScan("com.dwei.**.mapper")
//@EnableElasticsearchRepositories(basePackages = "com.dwei.*")
@EnableDubbo(scanBasePackages = {"com.dwei.*"})
public class AdminStartup {

    public static void main(String[] args) {
        ApplicationBoot.boot(AdminStartup.class).run(args);
    }

}
