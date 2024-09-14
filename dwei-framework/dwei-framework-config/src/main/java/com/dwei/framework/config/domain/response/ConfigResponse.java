package com.dwei.framework.config.domain.response;

import com.dwei.domain.entity.ConfigEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfigResponse {

    private Long id;

    /**
     * 参数名称
     */
    private String name;

    /**
     * 参数键
     */
    private String key;

    /**
     * 参数值
     */
    private String value;

    /**
     * 是否系统参数
     */
    private Boolean isSystem;

    /**
     * 备注
     */
    private String remark;

    public static ConfigResponse covert(ConfigEntity config) {
        return ConfigResponse.builder()
                .id(config.getId())
                .name(config.getName())
                .key(config.getKey())
                .value(config.getValue())
                .isSystem(config.getIsSystem())
                .remark(config.getRemark())
                .build();
    }

}
