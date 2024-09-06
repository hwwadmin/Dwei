package com.dwei.framework.auth.rbac.check;

import jakarta.servlet.http.HttpServletRequest;

/**
 * RBAC权限校验接口
 *
 * @author hww
 */
public interface RbacCheck {

    void checkAuth(HttpServletRequest request, Long userId, String token);

}
