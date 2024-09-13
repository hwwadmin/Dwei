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
 * 字典数据表
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "s_dict_data")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
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
    private Boolean enable;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}