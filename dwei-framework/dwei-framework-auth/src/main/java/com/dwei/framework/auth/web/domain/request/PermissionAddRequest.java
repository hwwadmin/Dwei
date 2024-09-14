package com.dwei.framework.auth.web.domain.request;

import com.dwei.core.annotation.StringTrim;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PermissionAddRequest {

    /**
     * 资源名称
     */
    @StringTrim
    @NotBlank
    private String name;

    /**
     * 权限标识
     */
    private String tag;

    /**
     * 资源路径
     */
    @StringTrim
    @NotBlank
    private String path;

    /**
     * 请求类型
     */
    private String method;

}
