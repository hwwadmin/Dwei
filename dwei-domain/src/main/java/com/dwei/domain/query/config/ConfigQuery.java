package com.dwei.domain.query.config;

import com.dwei.core.mvc.condition.annotation.QueryCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfigQuery {

    /**
     * 参数名称
     */
    @QueryCondition
    private String name;

}
