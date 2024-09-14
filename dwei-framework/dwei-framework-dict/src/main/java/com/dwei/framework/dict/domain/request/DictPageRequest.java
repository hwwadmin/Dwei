package com.dwei.framework.dict.domain.request;

import com.dwei.core.mvc.condition.annotation.QueryCondition;
import lombok.Data;

@Data
public class DictPageRequest {

    /**
     * 字典类型名称
     */
    @QueryCondition
    private String name;

    /**
     * 字典类型编码
     */
    @QueryCondition
    private String code;

}
