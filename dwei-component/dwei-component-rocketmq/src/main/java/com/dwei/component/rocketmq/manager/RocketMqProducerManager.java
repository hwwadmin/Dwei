package com.dwei.component.rocketmq.manager;

import com.dwei.common.utils.Assert;
import com.dwei.common.utils.Maps;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.component.rocketmq.adapter.RocketMqProducerAdapter;

import java.util.Map;

/**
 * 消息生产者管理器
 *
 * @author hww
 */
public class RocketMqProducerManager {

    /** 生产者缓存 */
    private static final Map<String, RocketMqProducerAdapter> cacheMap = Maps.ofSafe();

    /**
     * 发送消息
     */
    public static boolean sendMessage(String nameserver, String groupName, String topic, String msg) {
        var producer = get(nameserver, groupName);
        return producer.send(topic, msg);
    }

    /**
     * 发送消息
     */
    public static boolean sendMessage(String nameserver, String groupName, String topic, String tag, String msg) {
        var producer = get(nameserver, groupName);
        return producer.send(topic, tag, msg);
    }

    /**
     * 注册生产者
     */
    public synchronized static void register(RocketMqProducerAdapter producer) {
        Assert.nonNull(producer);

        var key = buildCacheKey(producer.getNameserver(), producer.getGroupName());
        Assert.isFalse(cacheMap.containsKey(key), "RocketMq消息生产器重复注册");
        producer.start();
        cacheMap.put(key, producer);
    }

    /**
     * 获取指定生产者
     */
    public static RocketMqProducerAdapter get(String nameserver, String groupName) {
        Assert.isStrNotBlank(nameserver);
        Assert.isStrNotBlank(groupName);

        var key = buildCacheKey(nameserver, groupName);
        Assert.isTrue(cacheMap.containsKey(key), "RocketMq消息生产器不存在");
        return cacheMap.get(key);
    }

    /**
     * 关闭生产者
     */
    public synchronized static void close(String nameserver, String groupName) {
        Assert.isStrNotBlank(nameserver);
        Assert.isStrNotBlank(groupName);

        get(nameserver, groupName).close();
        cacheMap.remove(buildCacheKey(nameserver, groupName));
    }

    /**
     * 关闭全部生产者
     */
    public synchronized static void closeAll() {
        if (ObjectUtils.isNull(cacheMap)) return;
        cacheMap.values().forEach(t -> close(t.getNameserver(), t.getGroupName()));
    }

    private static String buildCacheKey(String nameserver, String groupName) {
        return nameserver + "#" + groupName;
    }

}
