package com.dwei.framework.config.domain.request;

import com.dwei.core.mvc.condition.annotation.QueryCondition;
import lombok.Data;

@Data
public class ConfigPageRequest {

    /**
     * 参数名称
     */
    @QueryCondition
    private String name;

}
