package com.dwei.framework.auth.web.domain.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RbacRoleBindPermissionRequest {

    /**
     * 角色id
     */
    @NotNull
    private Long roleId;

    /**
     * 权限id
     */
    @NotNull
    private Long permissionId;

}
