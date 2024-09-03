package com.dwei.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.dwei.core.mvc.pojo.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 系统参数配置表
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="s_config")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ConfigEntity extends BaseEntity implements Serializable {

    /**
     * 参数名称
     */
    private String name;

    /**
     * 参数键
     */
    @TableField("`key`")
    private String key;

    /**
     * 参数值
     */
    private String value;

    /**
     * 是否系统参数
     */
    private Boolean isSystem;

    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}