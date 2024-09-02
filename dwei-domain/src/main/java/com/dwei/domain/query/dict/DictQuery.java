package com.dwei.domain.query.dict;

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
    private String name;

    /**
     * 字典类型编码
     */
    private String code;

}
