package com.dwei.framework.auth.token;

/**
 * 令牌API
 *
 * @author hww
 */
public interface TokenApi {

    /** 创建token */
    Token createToken(long userId);

    /** 获取当前用户token */
    Token getToken();

    /** 获取当前用户id */
    Long getUserId();

    /** 当前用户登出 */
    void logout();

    /** 指定用户强制登出 */
    void logout(long userId);

}
