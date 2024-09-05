package com.dwei.domain.query.dict;

import com.dwei.core.mvc.condition.annotation.QueryCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DictQuery {

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
