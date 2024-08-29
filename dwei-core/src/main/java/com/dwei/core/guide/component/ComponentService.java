package com.dwei.core.guide.component;

/**
 * 服务组件接口
 *
 * @author hww
 */
public interface ComponentService {

    /**
     * 启动组件
     */
    void start();

    /**
     * 停止组件
     */
    void stop();

    /**
     * 是否切换线程
     */
    default boolean isSwitchThread() {
        return false;
    }

    /**
     * 是否忽略异常
     */
    default boolean ignoreError() {
        return false;
    }

    /**
     * 获取组件名
     */
    default String getName() {
        return getClass().getSimpleName();
    }

}
