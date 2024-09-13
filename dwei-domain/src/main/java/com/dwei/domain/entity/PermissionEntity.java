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
 * RBAC#权限表
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "s_rbac_permission")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionEntity extends BaseEntity implements Serializable {

    /**
     * 资源名称
     */
    private String name;

    /**
     * 权限标识
     */
    private String tag;

    /**
     * 资源路径
     */
    private String path;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 是否启用
     */
    private Boolean enable;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}