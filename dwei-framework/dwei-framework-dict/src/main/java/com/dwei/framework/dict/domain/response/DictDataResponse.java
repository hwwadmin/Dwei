package com.dwei.framework.dict.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DictDataResponse {

    private Long id;

    /**
     * 字典类型编码
     */
    private String dictCode;

    /**
     * 排序
     */
    private Integer seq;

    /**
     * 标签
     */
    private String label;

    /**
     * 标签(英文)
     */
    private String labelEn;

    /**
     * 字典值
     */
    private String value;

    /**
     * 是否启用
     */
    private Boolean enable;

}
