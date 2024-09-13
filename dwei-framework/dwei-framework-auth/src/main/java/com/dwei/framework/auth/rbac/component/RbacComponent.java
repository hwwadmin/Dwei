package com.dwei.framework.auth.rbac.component;

import com.dwei.core.guide.component.ComponentService;
import com.dwei.framework.auth.rbac.utils.PermissionUtils;
import com.dwei.framework.auth.rbac.utils.RoleUtils;
import org.springframework.stereotype.Component;

/**
 * RBAC组件
 *
 * @author hww
 */
@Component
public class RbacComponent implements ComponentService {

    @Override
    public String getName() {
        return "RBAC组件";
    }

    @Override
    public void start() {
        RoleUtils.refresh(false);
        PermissionUtils.refresh(false);
    }

    @Override
    public void stop() {

    }

}
