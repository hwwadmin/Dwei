package com.dwei.core.guide.component;

import com.dwei.common.exception.UtilsException;
import com.dwei.core.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 组件服务统一管理类
 * 由该管理类统一管理所有系统组件服务
 *
 * @author hww
 */
@Slf4j
public class ComponentManager {

    private boolean isRun;
    private final Lock lock;
    private ComponentRegister register;

    private static class ComponentManagerHolder {
        private static final ComponentManager INSTANCE = new ComponentManager();
    }

    public static ComponentManager getInstance() {
        return ComponentManagerHolder.INSTANCE;
    }

    private ComponentManager() {
        this.isRun = false;
        this.lock = new ReentrantLock();
    }

    private ComponentRegister getRegister() {
        if (Objects.nonNull(register)) return register;
        synchronized (ComponentManager.class) {
            if (Objects.nonNull(register)) return register;
            register = SpringContextUtils.getBean(ComponentRegister.class);
            return register;
        }
    }

    /**
     * 启动组件管理
     */
    public void start() {
        this.lock.lock();
        try {
            if (this.isRun) return;
            log.info("开始启动系统服务组件，共需要启动[{}]个", getRegister().getComponent().size());
            getRegister().getComponent().forEach(t -> {
                if (t.isSwitchThread()) {
                    // 切换新线程
                    Thread thread = new Thread(() -> this.startComponent(t));
                    thread.setName("ComponentManager-Thread-" + t.getName());
                    thread.start();
                } else {
                    // 不切换新线程
                    this.startComponent(t);
                }
            });
            this.isRun = true;
        } catch (Exception e) {
            log.error("启动组件管理异常:{}", e.getMessage());
            throw UtilsException.exception(e);
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * 停止组件管理
     */
    public void stop() {
        this.lock.lock();
        try {
            if (!this.isRun) return;
            getRegister().getComponent().forEach(t -> {
                try {
                    t.stop();
                } catch (Exception e) {
                    // 停止失败打个日志忽略掉
                    log.error("停止组件[{}]失败", t.getName(), e);
                }
            });
            this.isRun = false;
        } catch (Exception e) {
            log.error("停止组件管理异常:{}", e.getMessage());
            throw UtilsException.exception(e);
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * 启动组件
     */
    private void startComponent(ComponentService componentService) {
        String name = componentService.getName();
        try {
            long time = System.currentTimeMillis();
            componentService.start();
            log.info("####### ------- [{}] start success , 耗时[{}ms] ------- #######", name, System.currentTimeMillis() - time);
        } catch (Exception e) {
            log.error("####### ------- [{}] error ------- #######", name, e);
            if (componentService.ignoreError()) {
                log.warn("服务组件[{}]配置了忽略异常，略过异常继续启动", name);
                return;
            }
            // 不忽略异常的话直接终止启动
            log.warn("服务组件[{}]启动异常，系统关停", name);
            System.exit(-9);
        }
    }

}
