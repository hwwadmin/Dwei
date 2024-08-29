package com.dwei.core.config.exception;

import com.dwei.core.config.exception.log.ExceptionHandlerLogManager;
import com.dwei.core.guide.component.ComponentService;
import com.dwei.core.utils.SpringContextUtils;
import org.springframework.stereotype.Component;

/**
 * 日志处理启动组件
 *
 * @author hww
 */
@Component
public class LogHandlerComponent implements ComponentService {

    @Override
    public void start() {
        var logManager = SpringContextUtils.getBean(ExceptionHandlerLogManager.class);
        logManager.refresh();
    }

    @Override
    public void stop() {

    }

    @Override
    public String getName() {
        return "日志处理启动组件";
    }

}
