package com.dwei.component.rocketmq.callback;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 默认的RocketMq消费回调函数实现
 * 只是把接收到的消息打印了日志
 *
 * @author hww
 */
@Slf4j
public class RocketMqConsumerDefaultCallback implements RocketMqConsumerCallback {

    @Override
    public void callback(List<MessageExt> messages, ConsumeConcurrentlyContext context) {
        messages.forEach(msg -> log.info("rocketMq receive topic[{}] - msg[{}]",
                msg.getTopic(), new String(msg.getBody(), StandardCharsets.UTF_8)));
    }

}
