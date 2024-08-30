package com.dwei.admin.user.domain.request;

import com.dwei.common.enums.SexEnum;
import com.dwei.core.annotation.StringTrim;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户注册 请求对象
 *
 * @author hww
 */
@Data
public class UserRegisterRequest {

    /** 用户名 */
    @StringTrim
    @NotBlank
    private String username;
    /** 密码 */
    @StringTrim
    @NotBlank
    private String password;
    /** 手机号码 */
    @StringTrim
    @NotBlank
    private String phone;
    /** 性别 */
    private SexEnum sex;

}
