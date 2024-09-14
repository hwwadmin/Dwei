package com.dwei.framework.auth.web.service;

import com.dwei.common.utils.Assert;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.domain.entity.RolePermissionEntity;
import com.dwei.domain.entity.UserRoleEntity;
import com.dwei.domain.repository.IRolePermissionRepository;
import com.dwei.domain.repository.IUserRoleRepository;
import com.dwei.framework.auth.rbac.utils.RolePermissionUtils;
import com.dwei.framework.auth.rbac.utils.UserRoleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RbacService {

    private final IUserRoleRepository userRoleRepository;
    private final IRolePermissionRepository rolePermissionRepository;

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void userBindRole(String userType, Long userId, Long roleId) {
        Assert.isStrNotBlank(userType);
        Assert.nonNull(userId);
        Assert.nonNull(roleId);

        var refList = userRoleRepository.find(userType, userId);
        for (var ref : refList) {
            // 如果已有该角色不重复添加
            if (ObjectUtils.equals(ref.getRoleId(), roleId)) return;
        }

        var entity = UserRoleEntity.builder()
                .userType(userType)
                .userId(userId)
                .roleId(roleId)
                .build();
        entity.init();

        userRoleRepository.save(entity);
        UserRoleUtils.refresh(userType, userId);
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void userUnBindRole(String userType, Long userId, Long roleId) {
        Assert.isStrNotBlank(userType);
        Assert.nonNull(userId);
        Assert.nonNull(roleId);

        var refList = userRoleRepository.find(userType, userId);
        var result = refList.stream()
                .filter(t -> ObjectUtils.equals(t.getRoleId(), roleId))
                .toList();
        if (ObjectUtils.isNull(result)) return;

        userRoleRepository.delBatch(result);
        UserRoleUtils.refresh(userType, userId);
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void roleBindPermission(Long roleId, Long permissionId) {
        Assert.nonNull(roleId);
        Assert.nonNull(permissionId);

        var refList = rolePermissionRepository.find(roleId);
        for (var ref : refList) {
            // 如果已有该权限不重复添加
            if (ObjectUtils.equals(ref.getPermissionId(), permissionId)) return;
        }

        var entity = RolePermissionEntity.builder()
                .roleId(roleId)
                .permissionId(permissionId)
                .build();
        entity.init();

        rolePermissionRepository.save(entity);
        RolePermissionUtils.refresh(roleId);
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void roleUnBindPermission(Long roleId, Long permissionId) {
        Assert.nonNull(roleId);
        Assert.nonNull(permissionId);

        var refList = rolePermissionRepository.find(roleId);
        var result = refList.stream()
                .filter(t -> ObjectUtils.equals(t.getPermissionId(), permissionId))
                .toList();
        if (ObjectUtils.isNull(result)) return;

        rolePermissionRepository.delBatch(result);
        RolePermissionUtils.refresh(roleId);
    }

}
