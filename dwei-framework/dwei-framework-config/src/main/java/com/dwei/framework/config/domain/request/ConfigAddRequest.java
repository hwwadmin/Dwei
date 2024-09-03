package com.dwei.framework.config.domain.request;

import com.dwei.core.annotation.StringTrim;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConfigAddRequest {

    /**
     * 参数名称
     */
    @StringTrim
    @NotBlank
    private String name;

    /**
     * 参数键
     */
    @StringTrim
    @NotBlank
    private String key;

    /**
     * 参数值
     */
    @StringTrim
    @NotBlank
    private String value;

    /**
     * 备注
     */
    private String remark;

}
