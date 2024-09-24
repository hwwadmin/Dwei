package com.dwei.component.rocketmq.config;

import com.dwei.common.pojo.Pair;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.common.utils.ReflectUtils;
import com.dwei.component.rocketmq.adapter.RocketMqConsumerAdapter;
import com.dwei.component.rocketmq.callback.RocketMqConsumerCallback;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConditionalOnProperty(value = "rocketmq.consumer.enable")
@ConfigurationProperties(value = "rocketmq.consumer")
@Data
public class RocketMqConsumerConfig {

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
        if (ObjectUtils.isNull(this.items)) return;
        items.forEach(t -> {
            Pair<String, String> acl = null;
            if (ObjectUtils.nonNull(t.getAccessKey()) && ObjectUtils.nonNull(t.getSecretKey())) {
                acl = new Pair<>(t.getAccessKey(), t.getSecretKey());
            }
            var callbackBean = (RocketMqConsumerCallback) ReflectUtils.newInstance(t.getBeanName());
            var consumerAdapter = new RocketMqConsumerAdapter(t.getNameserver(), t.getGroupName(), t.getTopic(), t.getTags(), acl, callbackBean);
            consumerAdapter.start();
        });
    }

}
