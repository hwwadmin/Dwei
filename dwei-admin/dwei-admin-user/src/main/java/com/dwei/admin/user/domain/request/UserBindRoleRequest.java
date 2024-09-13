package com.dwei.admin.user.domain.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserBindRoleRequest {

    /**
     * 用户id
     */
    @NotNull
    private Long userId;

    /**
     * 角色id
     */
    @NotNull
    private Long roleId;

}
