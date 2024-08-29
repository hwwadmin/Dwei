package com.dwei.core.guide.listener;

import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * 系统异常关闭事件监听
 *
 * @author hww
 */
public class ApplicationFailedEventListener implements ApplicationListener<ApplicationFailedEvent> {

    @Override
    public void onApplicationEvent(@NonNull ApplicationFailedEvent applicationEvent) {
        Throwable throwable = applicationEvent.getException();
        if (Objects.nonNull(throwable)) {
            throwable.printStackTrace(System.err);
        } else {
            System.err.println("启动应用错误，错误类型未知");
        }
    }

}
