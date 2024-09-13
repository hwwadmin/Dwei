package com.dwei.framework.auth.token;

import cn.dev33.satoken.stp.StpUtil;
import com.dwei.common.constants.AppConstants;
import com.dwei.common.utils.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 基于SA的token实现
 * 多用户通过用户id的前缀来识别，不要用多个StpLogic来操作导致系统复杂度增加
 *
 * @author hww
 */
@Component
@Slf4j
public class SaTokenApi implements TokenApi {

    @Override
    public Token createToken(String userType, long userId) {
        StpUtil.login(buildIdKey(userType, userId));
        log.info("用户登录，类型:[{}], id:[{}]", userType, userId);
        return getToken();
    }

    @Override
    public Token getToken() {
        return new Token()
                .setUserId(getUserId())
                .setToken(StpUtil.getTokenValue());
    }

    @Override
    public Long getUserId() {
        var idKey = StpUtil.getLoginId();
        Assert.nonNull(idKey, "用户未登录");
        return Long.valueOf(((String) idKey).split(AppConstants.UNDER_LINE)[1]);
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }

    @Override
    public void logout(String userType, long userId) {
        StpUtil.logout(buildIdKey(userType, userId));
    }

    @Override
    public void checkLogin() {
        StpUtil.checkLogin();
    }

    /**
     * 构建带用户类型的用户id
     */
    protected static String buildIdKey(String userType, long userId) {
        Assert.isStrNotBlank(userType, "登录类型缺失");
        return userType + AppConstants.UNDER_LINE + userId;
    }

}
