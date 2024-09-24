package com.dwei.component.rocketmq.config;

import com.dwei.common.pojo.Pair;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.component.rocketmq.adapter.RocketMqProducerAdapter;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConditionalOnProperty(value = "rocketmq.producer.enable")
@ConfigurationProperties(value = "rocketmq.producer")
@Data
public class RocketMqProducerConfig {

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
        if (ObjectUtils.isNull(this.items)) return;
        items.forEach(t -> {
            Pair<String, String> acl = null;
            if (ObjectUtils.nonNull(t.getAccessKey()) && ObjectUtils.nonNull(t.getSecretKey())) {
                acl = new Pair<>(t.getAccessKey(), t.getSecretKey());
            }
            new RocketMqProducerAdapter(t.getNameserver(), t.getGroupName(), acl);
        });
    }

}
