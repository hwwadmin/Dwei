package com.dwei.framework.auth.rbac.check;

import com.dwei.common.utils.Assert;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.utils.RequestUtils;
import com.dwei.framework.auth.rbac.utils.RolePermissionUtils;
import com.dwei.framework.auth.rbac.utils.UserRoleUtils;
import com.dwei.framework.auth.token.Token;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 默认的RBAC权限校验
 *
 * @author hww
 */
@Component("defaultRbacCheck")
@RequiredArgsConstructor
@Slf4j
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
        var method = RequestUtils.getMethod(request);
        for (var permission : permissionList) {
            if (ObjectUtils.equals(path, permission.getPath())) {
                // 成功匹配到满足的路径
                if (ObjectUtils.isNull(permission.getMethod())) return; // 没有设置方法的话直接通过

                // 校验方法类型
                if (RequestUtils.isSameMethod(method, permission.getMethod())) return;
            }
        }

        // 没有匹配到路径抛出异常
        log.warn("请求方法无权限! method:[{}], path:[{}]", method, path);
        Assert.ex();
    }

}
