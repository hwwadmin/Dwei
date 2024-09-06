package com.dwei.framework.auth.sa;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import com.dwei.common.enums.StatusCodeEnum;
import com.dwei.common.exception.IllegalValidatedException;
import com.dwei.framework.auth.rbac.RbacManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * SA令牌拦截器
 *
 * @author hww
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SaTokenInterceptor extends SaInterceptor {

    private static final String httpType4Operation = "OPTIONS";

    private final RbacManager rbacManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (Objects.equals(httpType4Operation, request.getMethod())) return true;

        // 用户认证
        checkUser(request);
        // 权限认证
        checkAuth(request);
        return true;
    }

    /**
     * 用户认证
     */
    private void checkUser(HttpServletRequest request) {
        try {
            // 用户认证使用sa的封装即可
            StpUtil.checkLogin();
        } catch (NotLoginException e) {
            throw IllegalValidatedException.exception(StatusCodeEnum.invalidToken.getCode(), StatusCodeEnum.invalidToken.getDefaultMessage(), e);
        }
    }

    /**
     * 权限认证
     */
    private void checkAuth(HttpServletRequest request) {
        try {
            rbacManager.check(request, StpUtil.getLoginIdAsLong(), StpUtil.getTokenValue());
        } catch (Exception e) {
            throw IllegalValidatedException.exception(StatusCodeEnum.unauthorized.getCode(), StatusCodeEnum.unauthorized.getDefaultMessage(), e);
        }
    }

}
