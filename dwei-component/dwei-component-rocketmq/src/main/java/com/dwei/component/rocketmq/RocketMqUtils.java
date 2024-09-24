package com.dwei.component.rocketmq;

import com.dwei.component.rocketmq.manager.RocketMqProducerManager;

/**
 * RocketMq工具类
 *
 * @author hww
 */
public class RocketMqUtils {

    /**
     * 发送消息
     */
    public static boolean sendMessage(String nameserver, String groupName, String topic, String msg) {
        return RocketMqProducerManager.sendMessage(nameserver, groupName, topic, msg);
    }

    /**
     * 发送消息
     */
    public static boolean sendMessage(String nameserver, String groupName, String topic, String tag, String msg) {
        return RocketMqProducerManager.sendMessage(nameserver, groupName, topic, tag, msg);
    }

}
