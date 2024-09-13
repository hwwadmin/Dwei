package com.dwei.framework.auth.web.domain.request;

import com.dwei.core.annotation.StringTrim;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RbacUserBindRoleRequest {

    /**
     * 用户类型
     */
    @StringTrim
    @NotBlank
    private String userType;

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
