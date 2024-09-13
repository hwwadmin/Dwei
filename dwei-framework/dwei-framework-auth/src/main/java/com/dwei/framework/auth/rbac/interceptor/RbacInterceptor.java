package com.dwei.framework.auth.rbac.interceptor;

import com.dwei.common.enums.StatusCodeEnum;
import com.dwei.common.exception.IllegalValidatedException;
import com.dwei.framework.auth.rbac.RbacManager;
import com.dwei.framework.auth.token.TokenApi;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

/**
 * RBAC拦截器
 *
 * @author hww
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RbacInterceptor implements HandlerInterceptor {

    private static final String httpType4Operation = "OPTIONS";

    private final RbacManager rbacManager;
    private final TokenApi tokenApi;

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
            tokenApi.checkLogin();
        } catch (Exception e) {
            throw IllegalValidatedException.exception(StatusCodeEnum.invalidToken.getCode(), StatusCodeEnum.invalidToken.getDefaultMessage(), e);
        }
    }

    /**
     * 权限认证
     */
    private void checkAuth(HttpServletRequest request) {
        try {
            rbacManager.check(request, tokenApi.getToken());
        } catch (Exception e) {
            throw IllegalValidatedException.exception(StatusCodeEnum.unauthorized.getCode(), StatusCodeEnum.unauthorized.getDefaultMessage(), e);
        }
    }

}
