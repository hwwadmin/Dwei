package com.dwei.framework.auth.web.domain.request;

import com.dwei.core.annotation.StringTrim;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleAddRequest {

    /**
     * 角色名称
     */
    @StringTrim
    @NotBlank
    private String name;

    /**
     * 角色标识
     */
    private String tag;

}
