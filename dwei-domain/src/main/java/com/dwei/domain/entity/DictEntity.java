package com.dwei.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.dwei.common.enums.YesNoEnum;
import com.dwei.core.mvc.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 字典类型表
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "s_dict")
@Data
@Accessors(chain = true)
public class DictEntity extends BaseEntity implements Serializable {

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

    /**
     * 是否启用
     */
    private YesNoEnum enable;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}