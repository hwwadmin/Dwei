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
 * 字典数据表
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "s_dict_data")
@Data
@Accessors(chain = true)
public class DictDataEntity extends BaseEntity implements Serializable {

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
    private YesNoEnum enable;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}