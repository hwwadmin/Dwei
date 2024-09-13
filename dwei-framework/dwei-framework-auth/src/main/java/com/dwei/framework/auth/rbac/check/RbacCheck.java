package com.dwei.framework.auth.rbac.check;

import com.dwei.framework.auth.token.Token;
import jakarta.servlet.http.HttpServletRequest;

/**
 * RBAC权限校验接口
 *
 * @author hww
 */
public interface RbacCheck {

    void checkAuth(HttpServletRequest request, Token token);

}
