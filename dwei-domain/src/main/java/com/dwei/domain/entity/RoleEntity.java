package com.dwei.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.mvc.pojo.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * RBAC#角色表
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "s_rbac_role")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity extends BaseEntity implements Serializable {

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色标识
     */
    private String tag;

    /**
     * 是否启用
     */
    private Boolean enable;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 是否管理员
     */
    @JsonIgnore
    public boolean isAdmin() {
        return ObjectUtils.equals(tag, "admin");
    }

}