package com.dwei.framework.dict.domain.request;

import com.dwei.core.annotation.StringTrim;
import com.dwei.core.mvc.pojo.request.IdRequest;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DictDataUpdateRequest extends IdRequest {

    /**
     * 排序
     */
    @Min(0)
    private Integer seq;

    /**
     * 标签
     */
    @StringTrim
    private String label;

    /**
     * 标签(英文)
     */
    @StringTrim
    private String labelEn;

    /**
     * 字典值
     */
    @StringTrim
    private String value;

    /**
     * 是否启用
     */
    private Boolean enable;

}
