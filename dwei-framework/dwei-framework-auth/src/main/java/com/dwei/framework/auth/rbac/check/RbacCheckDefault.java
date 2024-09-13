package com.dwei.framework.auth.rbac.check;

import com.dwei.framework.auth.token.Token;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 默认的RBAC权限校验
 *
 * @author hww
 */
@Component("defaultRbacCheck")
@Slf4j
public class RbacCheckDefault implements RbacCheck {

    @Override
    public void checkAuth(HttpServletRequest request, Token token) {
        log.info("权限校验通过~");
    }

}
