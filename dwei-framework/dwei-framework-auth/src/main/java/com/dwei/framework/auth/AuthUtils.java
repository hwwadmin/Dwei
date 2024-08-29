package com.dwei.framework.auth;

import com.dwei.core.utils.SpringContextUtils;
import com.dwei.framework.auth.token.TokenApi;

import java.util.Objects;

/**
 * 权限工具类
 *
 * @author hww
 */
public abstract class AuthUtils {

    private static TokenApi tokenApi;

    private AuthUtils() {

    }

    public static TokenApi getTokenApi() {
        if (Objects.nonNull(tokenApi)) return tokenApi;
        synchronized (AuthUtils.class) {
            if (Objects.nonNull(tokenApi)) return tokenApi;
            tokenApi = SpringContextUtils.getBean(TokenApi.class);
            return tokenApi;
        }
    }

    public static Long getUserId() {
        return tokenApi.getUserId();
    }

}
