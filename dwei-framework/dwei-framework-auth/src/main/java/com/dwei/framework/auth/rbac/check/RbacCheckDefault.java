package com.dwei.framework.auth.rbac.check;

import com.dwei.common.utils.Assert;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.utils.RequestUtils;
import com.dwei.framework.auth.rbac.utils.RolePermissionUtils;
import com.dwei.framework.auth.rbac.utils.UserRoleUtils;
import com.dwei.framework.auth.token.Token;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 默认的RBAC权限校验
 *
 * @author hww
 */
@Component("defaultRbacCheck")
@RequiredArgsConstructor
public class RbacCheckDefault implements RbacCheck {

    @Override
    public void checkAuth(HttpServletRequest request, Token token) {
        // role
        var roleList = UserRoleUtils.get(token.getUserType(), token.getUserId());
        Assert.isNotEmpty(roleList);

        // admin管理员用户拥有全部权限
        for (var role : roleList) if (role.isAdmin()) return;

        // permission
        var permissionList = roleList.stream()
                .map(role -> RolePermissionUtils.get(role.getId()))
                .flatMap(List::stream)
                .toList();
        Assert.isNotEmpty(permissionList);

        // check
        var path = RequestUtils.getUri(request);
        for (var permission : permissionList) if (ObjectUtils.equals(path, permission.getPath())) return;
        Assert.ex(); // 没有匹配到路径抛出异常
    }

}
