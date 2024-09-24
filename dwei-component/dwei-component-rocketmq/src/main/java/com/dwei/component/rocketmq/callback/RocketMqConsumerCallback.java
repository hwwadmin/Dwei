package com.dwei.component.rocketmq.callback;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * ROCKETMQ消费回调函数接口
 *
 * @author hww
 */
public interface RocketMqConsumerCallback {

    void callback(List<MessageExt> messages, ConsumeConcurrentlyContext context);

}
