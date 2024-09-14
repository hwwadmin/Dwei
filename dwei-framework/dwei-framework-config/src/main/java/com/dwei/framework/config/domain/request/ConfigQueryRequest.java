package com.dwei.framework.config.domain.request;

import com.dwei.core.mvc.condition.annotation.QueryCondition;
import lombok.Data;

@Data
public class ConfigQueryRequest {

    /**
     * 参数名称
     */
    @QueryCondition
    private String name;

}
