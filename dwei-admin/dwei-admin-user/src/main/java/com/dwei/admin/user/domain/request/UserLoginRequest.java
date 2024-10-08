package com.dwei.admin.user.domain.request;

import com.dwei.core.annotation.StringTrim;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户登录 请求对象
 *
 * @author hww
 */
@Data
public class UserLoginRequest {

    @StringTrim
    @NotBlank
    private String username;
    @StringTrim
    @NotBlank
    private String password;

}
