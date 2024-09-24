package com.dwei.component.rocketmq.config;

import com.dwei.common.pojo.Pair;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.component.rocketmq.adapter.RocketMqProducerAdapter;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConditionalOnProperty(value = "rocketmq.producer.enable")
@ConfigurationProperties(value = "rocketmq.producer")
@Data
@Slf4j
public class RocketMqProducerConfig {

    private boolean enable;
    private List<RocketMqProducerConfigItem> items;

    @Data
    public static class RocketMqProducerConfigItem {
        @NotBlank
        private String nameserver;
        @NotBlank
        private String groupName;
        private String accessKey;
        private String secretKey;
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
                var producerAdapter = new RocketMqProducerAdapter(t.getNameserver(), t.getGroupName(), acl);
                producerAdapter.start();
            } catch (Exception e) {
                log.error("初始化RocketMqProducer失败", e);
            }
        });
    }

}
