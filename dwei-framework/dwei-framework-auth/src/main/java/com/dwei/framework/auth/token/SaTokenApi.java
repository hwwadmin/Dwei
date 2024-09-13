package com.dwei.framework.auth.token;

import cn.dev33.satoken.stp.StpUtil;
import com.dwei.common.utils.Assert;
import com.dwei.framework.auth.rbac.utils.UserRoleUtils;
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
        StpUtil.login(UserRoleUtils.buildIdKey(userType, userId));
        log.info("用户登录，类型:[{}], id:[{}]", userType, userId);
        return getToken();
    }

    @Override
    public Token getToken() {
        var idKey = StpUtil.getLoginId();
        Assert.nonNull(idKey, "用户未登录");
        var pair = UserRoleUtils.parseIdKey((String) idKey);
        return new Token()
                .setIdKey(String.valueOf(idKey))
                .setUserType(pair.getFirst())
                .setUserId(pair.getSecond())
                .setToken(StpUtil.getTokenValue());
    }

    @Override
    public Long getUserId() {
        var idKey = StpUtil.getLoginId();
        Assert.nonNull(idKey, "用户未登录");
        return UserRoleUtils.parseIdKey((String) idKey).getSecond();
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }

    @Override
    public void logout(String userType, long userId) {
        StpUtil.logout(UserRoleUtils.buildIdKey(userType, userId));
    }

    @Override
    public void checkLogin() {
        StpUtil.checkLogin();
    }

}
