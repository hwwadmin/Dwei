package com.dwei.framework.auth.token;

import cn.dev33.satoken.stp.StpUtil;
import com.dwei.common.utils.Assert;
import org.springframework.stereotype.Component;

/**
 * 基于SA的token实现
 *
 * @author hww
 */
@Component
public class SaTokenApi implements TokenApi {

    @Override
    public Token createToken(long userId) {
        StpUtil.login(userId);
        return getToken();
    }

    @Override
    public Token getToken() {
        return new Token().setUserId(getUserId()).setToken(StpUtil.getTokenValue());
    }

    @Override
    public Long getUserId() {
        var userId = StpUtil.getLoginId();
        Assert.nonNull(userId, "用户未登录");
        return Long.valueOf((String) userId);
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }

    @Override
    public void logout(long userId) {
        StpUtil.logout(userId);
    }

}
