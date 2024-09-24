package com.dwei.component.rocketmq.config;

import cn.hutool.extra.spring.SpringUtil;
import com.dwei.common.pojo.Pair;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.component.rocketmq.adapter.RocketMqConsumerAdapter;
import com.dwei.component.rocketmq.callback.RocketMqConsumerCallback;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConditionalOnProperty(value = "rocketmq.consumer.enable")
@ConfigurationProperties(value = "rocketmq.consumer")
@Data
@Slf4j
public class RocketMqConsumerConfig {

    private boolean enable;
    private List<RocketMqConsumerConfigItem> items;

    @Data
    public static class RocketMqConsumerConfigItem {
        @NotBlank
        private String nameserver;
        @NotBlank
        private String groupName;
        private String accessKey;
        private String secretKey;
        @NotBlank
        private String topic;
        private List<String> tags;

        /**
         * 消费回调函数实现类
         *
         * @see RocketMqConsumerCallback
         */
        @NotBlank
        private String beanName;
    }

    @PostConstruct
    public void init() {
        if (!enable) return;
        if (ObjectUtils.isNull(this.items)) return;
        items.forEach(t -> {
            try {
                Pair<String, String> acl = null;
                if (ObjectUtils.nonNull(t.getAccessKey()) && ObjectUtils.nonNull(t.getSecretKey())) {
                    acl = new Pair<>(t.getAccessKey(), t.getSecretKey());
                }
                var callbackBean = SpringUtil.getBean(t.beanName, RocketMqConsumerCallback.class);
                var consumerAdapter = new RocketMqConsumerAdapter(t.getNameserver(), t.getGroupName(), t.getTopic(), t.getTags(), acl, callbackBean);
                consumerAdapter.start();
            } catch (Exception e) {
                log.error("初始化RocketMqConsumer失败", e);
            }
        });
    }

}
