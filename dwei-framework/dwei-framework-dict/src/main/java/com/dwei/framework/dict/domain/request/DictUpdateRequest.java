package com.dwei.framework.dict.domain.request;

import com.dwei.core.annotation.StringTrim;
import com.dwei.core.mvc.pojo.request.IdRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DictUpdateRequest extends IdRequest {

    /**
     * 字典类型名称
     */
    @StringTrim
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enable;

}
