package com.dwei.framework.config.domain.response;

import lombok.*;

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

}
