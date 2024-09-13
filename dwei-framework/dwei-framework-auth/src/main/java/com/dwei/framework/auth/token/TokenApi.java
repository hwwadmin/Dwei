package com.dwei.framework.auth.token;

/**
 * 令牌API
 *
 * @author hww
 */
public interface TokenApi {

    /** 创建token */
    Token createToken(String userType, long userId);

    /** 获取当前用户token */
    Token getToken();

    /** 获取当前用户id */
    Long getUserId();

    /** 当前用户登出 */
    void logout();

    /** 指定用户强制登出 */
    void logout(String userType, long userId);

    /** 检验当前会话是否已经登录，如未登录，则抛出异常 */
    void checkLogin();

}
