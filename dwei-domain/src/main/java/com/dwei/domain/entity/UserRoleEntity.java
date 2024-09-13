package com.dwei.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dwei.core.mvc.pojo.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * RBAC#用户-角色关联表
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "s_rbac_user_role")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleEntity extends BaseEntity implements Serializable {

    /**
     * 用户系统类型
     */
    private String userType;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}