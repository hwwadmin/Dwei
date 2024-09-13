package com.dwei.framework.auth.rbac.utils;

import com.dwei.common.utils.Lists;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.mvc.repository.ListCacheRepository;
import com.dwei.core.utils.SpringContextUtils;
import com.dwei.domain.entity.PermissionEntity;
import com.dwei.domain.entity.RolePermissionEntity;
import com.dwei.domain.mapper.RolePermissionMapper;
import com.dwei.domain.repository.IRolePermissionRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 角色权限工具类
 *
 * @author hww
 */
public class RolePermissionUtils {

    private static final String serviceName = "role2Permission";
    private final ListCacheRepository<IRolePermissionRepository, RolePermissionMapper, RolePermissionEntity> cacheRepository;

    private static class RolePermissionUtilsHolder {
        private static final RolePermissionUtils instance = new RolePermissionUtils();
    }

    private static RolePermissionUtils getInstance() {
        return RolePermissionUtilsHolder.instance;
    }

    private RolePermissionUtils() {
        cacheRepository = new ListCacheRepository<>(
                SpringContextUtils.getBean(IRolePermissionRepository.class),
                serviceName,
                t -> String.valueOf(t.getRoleId()),
                (code, repository) -> Optional.ofNullable(repository.find(Long.valueOf(code))));
    }

    public static List<PermissionEntity> get(Long roleId) {
        var rolePermission = getInstance().cacheRepository.get(String.valueOf(roleId));
        if (ObjectUtils.isNull(rolePermission)) return Lists.of();
        var permissionIds = rolePermission.stream()
                .map(RolePermissionEntity::getPermissionId)
                .distinct()
                .toList();
        if (ObjectUtils.isNull(permissionIds)) return Lists.of();

        return permissionIds.stream().map(PermissionUtils::get).collect(Collectors.toList());
    }

    /**
     * 全量刷新
     *
     * @param compel 是否强制刷新
     */
    public synchronized static void refresh(boolean compel) {
        getInstance().cacheRepository.refresh(compel);
    }

    /**
     * 指定刷新
     */
    public synchronized static void refresh(Long roleId) {
        getInstance().cacheRepository.refresh(String.valueOf(roleId));
    }

}
