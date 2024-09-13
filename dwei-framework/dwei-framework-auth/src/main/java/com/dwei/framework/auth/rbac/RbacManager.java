package com.dwei.framework.auth.rbac;

import com.dwei.core.utils.SpringContextUtils;
import com.dwei.framework.auth.rbac.check.RbacCheck;
import com.dwei.framework.auth.token.Token;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * RBAC管理器
 *
 * @author hww
 */
@Component
public class RbacManager {

    @Value("${dwei.api.rbac-check-bean:defaultRbacCheck}")
    private String rbacCheckBeanName;

    public void check(HttpServletRequest request, Token token) {
        var rbacCheck = SpringContextUtils.getBean(rbacCheckBeanName, RbacCheck.class);
        rbacCheck.checkAuth(request, token);
    }

}
