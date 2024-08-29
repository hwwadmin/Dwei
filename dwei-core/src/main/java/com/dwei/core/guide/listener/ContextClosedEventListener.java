package com.dwei.core.guide.listener;

import com.dwei.core.guide.component.ComponentManager;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.lang.NonNull;

/**
 * 系统关闭事件监听
 *
 * @author hww
 */
public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(@NonNull ContextClosedEvent applicationEvent) {
        System.out.println("关闭系统");
        try {
            ComponentManager.getInstance().stop();
        } catch (Exception e) {
            System.exit(-9);
        }
    }

}
