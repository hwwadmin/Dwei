package com.dwei.component.rocketmq.manager;

import com.dwei.common.utils.Assert;
import com.dwei.common.utils.Maps;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.component.rocketmq.adapter.RocketMqConsumerAdapter;

import java.util.Map;

/**
 * 消息消费者管理器
 *
 * @author hww
 */
public class RocketMqConsumerManager {

    /** 消费者缓存 */
    private static final Map<String, RocketMqConsumerAdapter> cacheMap = Maps.ofSafe();

    /**
     * 注册消费者
     */
    public synchronized static void register(RocketMqConsumerAdapter consumer) {
        Assert.nonNull(consumer);

        var key = buildCacheKey(consumer.getNameserver(), consumer.getGroupName(), consumer.getTopic(), consumer.getTag());
        cacheMap.put(key, consumer);
    }

    /**
     * 获取指定消费者
     */
    public static RocketMqConsumerAdapter get(String nameserver, String groupName, String topic, String tag) {
        Assert.isStrNotBlank(nameserver);
        Assert.isStrNotBlank(groupName);

        var key = buildCacheKey(nameserver, groupName, topic, tag);
        Assert.isTrue(cacheMap.containsKey(key), "RocketMq消息生产器不存在");
        return cacheMap.get(key);
    }

    /**
     * 关闭消费者
     */
    public synchronized static void close(String nameserver, String groupName, String topic, String tag) {
        Assert.isStrNotBlank(nameserver);
        Assert.isStrNotBlank(groupName);

        get(nameserver, groupName, topic, tag).close();
        cacheMap.remove(buildCacheKey(nameserver, groupName, topic, tag));
    }

    /**
     * 关闭全部消费者
     */
    public synchronized static void closeAll() {
        if (ObjectUtils.isNull(cacheMap)) return;
        cacheMap.values().forEach(t -> close(t.getNameserver(), t.getGroupName(), t.getTopic(), t.getTag()));
    }

    private static String buildCacheKey(String nameserver, String groupName, String topic, String tag) {
        return nameserver + "#" + groupName + "#" + topic + "#" + tag;
    }

}
