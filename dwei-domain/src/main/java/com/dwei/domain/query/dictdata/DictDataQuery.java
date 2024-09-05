package com.dwei.domain.query.dictdata;

import com.dwei.core.mvc.condition.annotation.QueryCondition;
import com.dwei.core.mvc.condition.info.QueryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DictDataQuery {

    /** 字典类型编码 */
    @QueryCondition
    private String dictCode;
    @QueryCondition(value = QueryType.IN, name = "dictCode")
    private List<String> dictCodeIn;

}
