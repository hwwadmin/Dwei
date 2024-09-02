package com.dwei.domain.query.dictdata;

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
    private String dictCode;
    private List<String> dictCodeIn;

}
