package com.dwei.component.rocketmq.adapter;

import com.dwei.common.exception.UtilsException;
import com.dwei.common.pojo.Pair;
import com.dwei.common.utils.Assert;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.component.rocketmq.callback.RocketMqConsumerCallback;
import com.dwei.component.rocketmq.manager.RocketMqConsumerManager;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;

import java.util.List;
import java.util.stream.Collectors;

/**
 * RocketMq消费者适配器
 *
 * @author hww
 */
@Slf4j
public class RocketMqConsumerAdapter {

    /** 是否启动 */
    private boolean isRun = false;
    @Getter
    private final String nameserver;
    @Getter
    private final String groupName;
    @Getter
    private final String topic;
    @Getter
    private final String tag;
    private final MQPushConsumer consumer;

    public RocketMqConsumerAdapter(String nameserver, String groupName, String topic, List<String> tags,
                                   Pair<String, String> acl, RocketMqConsumerCallback consumerCallback) {
        Assert.isStrNotBlank(nameserver);
        Assert.isStrNotBlank(groupName);
        Assert.isStrNotBlank(topic);
        Assert.nonNull(consumerCallback);

        this.nameserver = nameserver;
        this.groupName = groupName;
        this.topic = topic;
        this.tag = buildTag(tags);
        this.consumer = buildConsumer(this.nameserver, this.groupName, this.topic, this.tag, acl, consumerCallback);

        // 注册消费者
        RocketMqConsumerManager.register(this);
    }

    /**
     * 构建消费者
     */
    @SneakyThrows
    private static MQPushConsumer buildConsumer(String nameserver, String groupName, String topic, String tag,
                                                   Pair<String, String> acl, RocketMqConsumerCallback consumerCallback) {
        DefaultMQPushConsumer consumer;
        if (ObjectUtils.isNull(acl)) {
            consumer = new DefaultMQPushConsumer(groupName);
        } else {
            Assert.isTrue(acl.isNotNull());
            consumer = new DefaultMQPushConsumer(
                    groupName,
                    new AclClientRPCHook(new SessionCredentials(acl.getFirst(), acl.getSecond())),
                    new AllocateMessageQueueAveragely()
            );
        }
        consumer.setNamesrvAddr(nameserver);
        consumer.subscribe(topic, tag);
        consumer.registerMessageListener((MessageListenerConcurrently) (messages, context) -> {
            log.info("RocketMQ#Consumer[{} - {}] receive messages: {}", nameserver, groupName, messages);
            try {
                consumerCallback.callback(messages, context);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } catch (Exception e) {
                // MQ消息消费异常并且没有函数内部处理的情况统一处理
                log.error("RocketMQ#Consumer消息消费异常");
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        });
        return consumer;
    }

    public synchronized void start() {
        if (isRun) return;
        try {
            consumer.start();
        } catch (Exception e) {
            throw UtilsException.exception("RocketMq消费者启动失败", e);
        }
        isRun = true;
        log.info("####### ------- [{} - {}] RocketMq消费者[启动]", nameserver, groupName);
    }

    public synchronized void close() {
        if (!isRun) return;
        try {
            consumer.shutdown();
        } catch (Exception e) {
            throw UtilsException.exception("RocketMq消费者关闭失败", e);
        }
        isRun = false;
        log.info("####### ------- [{} - {}] RocketMq消费者[关闭]", nameserver, groupName);
    }

    private static String buildTag(List<String> tags) {
        if (ObjectUtils.isNull(tags)) return "*";
        return tags.stream().filter(ObjectUtils::nonNull).collect(Collectors.joining("||"));
    }

}
