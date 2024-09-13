package com.dwei.framework.auth.rbac.check;

import com.dwei.common.utils.Assert;
import com.dwei.common.utils.Lists;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.mvc.pojo.entity.BaseEntity;
import com.dwei.core.utils.RequestUtils;
import com.dwei.domain.entity.PermissionEntity;
import com.dwei.domain.entity.RoleEntity;
import com.dwei.domain.entity.RolePermissionEntity;
import com.dwei.domain.entity.UserRoleEntity;
import com.dwei.domain.repository.IRolePermissionRepository;
import com.dwei.domain.repository.IUserRoleRepository;
import com.dwei.framework.auth.rbac.utils.PermissionUtils;
import com.dwei.framework.auth.rbac.utils.RoleUtils;
import com.dwei.framework.auth.token.Token;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 默认的RBAC权限校验
 *
 * @author hww
 */
@Component("defaultRbacCheck")
@RequiredArgsConstructor
public class RbacCheckDefault implements RbacCheck {

    private final IUserRoleRepository userRoleRepository;
    private final IRolePermissionRepository rolePermissionRepository;

    /**
     * 用户角色缓存
     */
    private final Cache<String, List<Long>> user2role = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    /**
     * 角色权限缓存
     */
    private final Cache<Long, List<Long>> role2permission = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    @Override
    public void checkAuth(HttpServletRequest request, Token token) {
        // role
        queryUserRole(token);
        var roleIds = user2role.getIfPresent(token.getIdKey());
        Assert.isNotEmpty(roleIds);
        var roleList = Lists.filterNull(Lists.map(roleIds, RoleUtils::get))
                .stream()
                .filter(RoleEntity::getEnable)
                .toList();
        Assert.isNotEmpty(roleList);

        // admin管理员用户拥有全部权限
        for (var role : roleList) if (role.isAdmin()) return;

        // permission
        roleList.stream().map(BaseEntity::getId).forEach(this::queryRolePermission);
        var permissionIds = roleIds.stream()
                .map(role2permission::getIfPresent)
                .filter(ObjectUtils::nonNull)
                .flatMap(List::stream)
                .distinct()
                .toList();
        Assert.isNotEmpty(permissionIds);
        var permissionList = Lists.filterNull(Lists.map(permissionIds, PermissionUtils::get))
                .stream()
                .filter(PermissionEntity::getEnable)
                .toList();
        Assert.isNotEmpty(permissionList);

        // check
        var path = RequestUtils.getUri(request);
        for (var permission : permissionList) if (ObjectUtils.equals(path, permission.getPath())) return;
        Assert.ex();
    }

    /**
     * 从db查询用户角色并缓存
     */
    private void queryUserRole(Token token) {
        if (ObjectUtils.nonNull(user2role.getIfPresent(token.getIdKey()))) return;

        var data = userRoleRepository.find(token.getUserType(), token.getUserId());
        user2role.put(token.getIdKey(), Lists.map(data, UserRoleEntity::getRoleId));
    }

    /**
     * 从db查询角色权限并缓存
     */
    private void queryRolePermission(long roleId) {
        if (ObjectUtils.nonNull(role2permission.getIfPresent(roleId))) return;

        var data = rolePermissionRepository.find(roleId);
        role2permission.put(roleId, Lists.map(data, RolePermissionEntity::getPermissionId));
    }

}
