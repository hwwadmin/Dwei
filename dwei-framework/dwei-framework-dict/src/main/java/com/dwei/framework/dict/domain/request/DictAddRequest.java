package com.dwei.framework.dict.domain.request;

import com.dwei.core.annotation.StringTrim;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DictAddRequest {

    /**
     * 字典类型名称
     */
    @StringTrim
    @NotBlank
    private String name;

    /**
     * 字典类型编码
     */
    @StringTrim
    @NotBlank
    private String code;

    /**
     * 备注
     */
    private String remark;

}
