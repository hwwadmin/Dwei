package com.dwei.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dwei.core.mvc.pojo.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 字典类型表
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "s_dict")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
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
    private Boolean enable;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}