package com.dwei.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

import com.dwei.core.mvc.pojo.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * RBAC#角色-权限关联表
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "s_rbac_role_permission")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class RolePermissionEntity extends BaseEntity implements Serializable {

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 权限id
     */
    private Long permissionId;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}