package com.dwei.framework.auth.token;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 令牌
 *
 * @author hww
 */
@Data
@Accessors(chain = true)
public class Token {

    /** 令牌的缓存id */
    private String idKey;
    /** 登录用户类型 */
    private String userType;
    /** 登录用户id */
    private Long userId;
    /** 令牌 */
    private String token;
    /** 刷新令牌 */
    private String refreshToken;

}
