package com.dwei.core.mvc.password;

/**
 * 密码加密接口
 *
 * @author hww
 */
public interface PasswordEncoder {

    /**
     * 加密原始密码
     */
    String encode(CharSequence rawPassword);

    /**
     * 验证原始密码是否和加密后的密码匹配
     */
    boolean matches(CharSequence rawPassword, String encodedPassword);

    /**
     *  = matches
     */
    default boolean checkPw(CharSequence inputPassword, String dbPassword) {
        return matches(inputPassword, dbPassword);
    }

    /**
     * 验证加密后的密码是否需要再次加密
     *
     * @return 如果需要返回 true，否则默认返回 false
     */
    default boolean upgradeEncoding(String encodedPassword) {
        return false;
    }

}
