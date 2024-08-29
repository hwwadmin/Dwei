package com.dwei.core.config.exception.log;

import com.dwei.common.utils.Lists;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.utils.SpringContextUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 系统崩溃日志管理类
 *
 * @author hww
 */
@Component
@Slf4j
public class ExceptionHandlerLogManager {

    @Value("${dwei.logs:defaultExceptionHandlerLog}")
    private List<String> beanNames;

    @Getter
    private List<ExceptionHandlerLog> handlerLogs;

    public synchronized void refresh() {
        if (ObjectUtils.nonNull(handlerLogs)) {
            handlerLogs.clear();
        } else {
            this.handlerLogs = Lists.of();
        }
        this.beanNames.forEach(it -> {
            this.handlerLogs.add(SpringContextUtils.getBean(it));
            log.info("加载日志处理器[{}]", it);
        });
    }

}
