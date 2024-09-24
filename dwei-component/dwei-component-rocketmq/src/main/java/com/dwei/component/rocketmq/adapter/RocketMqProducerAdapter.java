package com.dwei.component.rocketmq.adapter;

import com.dwei.common.exception.UtilsException;
import com.dwei.common.pojo.Pair;
import com.dwei.common.utils.Assert;
import com.dwei.component.rocketmq.manager.RocketMqProducerManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.Objects;

/**
 * RocketMq生产者适配器
 *
 * @author hww
 */
@Slf4j
public class RocketMqProducerAdapter {

    /** 是否启动 */
    private boolean isRun = false;
    @Getter
    private final String nameserver;
    @Getter
    private final String groupName;
    private final MQProducer producer;

    public RocketMqProducerAdapter(String nameserver, String groupName, Pair<String, String> acl) {
        Assert.isStrNotBlank(nameserver);
        Assert.isStrNotBlank(groupName);

        this.nameserver = nameserver;
        this.groupName = groupName;
        this.producer = buildProducer(this.nameserver, this.groupName, acl);

        // 注册生产者
        RocketMqProducerManager.register(this);
    }

    /**
     * 构建生产者
     */
    private static MQProducer buildProducer(String nameserver, String groupName, Pair<String, String> acl) {
        DefaultMQProducer producer;
        if (Objects.isNull(acl)) {
            producer = new DefaultMQProducer(groupName);
        } else {
            Assert.isTrue(acl.isNotNull());
            producer = new DefaultMQProducer(
                    groupName,
                    new AclClientRPCHook(new SessionCredentials(acl.getFirst(), acl.getSecond())) // ACL权限
            );
        }
        producer.setNamesrvAddr(nameserver);
        return producer;
    }

    public synchronized void start() {
        if (isRun) return;
        try {
            producer.start();
        } catch (Exception e) {
            throw UtilsException.exception("RocketMq生产者启动失败", e);
        }
        isRun = true;
        log.info("####### ------- [{} - {}] RocketMq生产者[启动]", nameserver, groupName);
    }

    public synchronized void close() {
        if (!isRun) return;
        try {
            producer.shutdown();
        } catch (Exception e) {
            throw UtilsException.exception("RocketMq生产者关闭失败", e);
        }
        isRun = false;
        log.info("####### ------- [{} - {}] RocketMq生产者[关闭]", nameserver, groupName);
    }

    // ---------------- send message ---------------- //

    public boolean send(String topic, String msg) {
        return send(topic, null, msg);
    }

    public boolean send(String topic, String tag, String msg) {
        Assert.isTrue(isRun, "RocketMQ#Producer未启动");
        Assert.isStrNotBlank(topic);
        Assert.isStrNotBlank(msg);

        SendResult sendResult = null;
        try {
            Message message = new Message(topic, tag, msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
            sendResult = producer.send(message, 3000); // 默认3s超时
            log.info("RocketMQ#Producer[{} - {}] send msg: {}", nameserver, groupName, msg);
        } catch (Exception e) {
            // 异常不抛出返回发送失败即可
            log.error("RocketMqMsg#消息发送异常", e);
        }
        if (Objects.isNull(sendResult)) return false;
        return Objects.equals(sendResult.getSendStatus(), SendStatus.SEND_OK);
    }

}
