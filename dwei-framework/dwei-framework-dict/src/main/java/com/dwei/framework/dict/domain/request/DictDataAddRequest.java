package com.dwei.framework.dict.domain.request;

import com.dwei.core.annotation.StringTrim;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DictDataAddRequest {

    /**
     * 字典类型编码
     */
    @StringTrim
    @NotBlank
    private String dictCode;

    /**
     * 排序
     */
    @NotNull
    @Min(0)
    private Integer seq;

    /**
     * 标签
     */
    @StringTrim
    @NotBlank
    private String label;

    /**
     * 标签(英文)
     */
    @StringTrim
    @NotBlank
    private String labelEn;

    /**
     * 字典值
     */
    @StringTrim
    @NotBlank
    private String value;

}
