package com.dwei.framework.dict.domain.request;

import lombok.Data;

@Data
public class DictAddRequest {

    /**
     * 字典类型名称
     */
    private String name;

    /**
     * 字典类型编码
     */
    private String code;

    /**
     * 备注
     */
    private String remark;

}
