package com.dwei.core.guide;

import com.dwei.core.guide.component.ComponentManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 核心框架启动器
 *
 * @author hww
 */
@Component
@Order(StarterOrder.WEB_FRAMEWORK_STARTER)
@Slf4j
public class WebFrameworkStarter implements ApplicationRunner {

    /**
     * 启动核心框架组件
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        long startTime = System.currentTimeMillis();
        // 组件启动管理器
        ComponentManager.getInstance().start();
        log.info("系统核心组件启动完成，耗时：{} 秒。", (System.currentTimeMillis() - startTime) / 1000);
    }

}
