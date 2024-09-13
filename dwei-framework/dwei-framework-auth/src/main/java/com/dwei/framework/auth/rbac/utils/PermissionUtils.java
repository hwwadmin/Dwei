package com.dwei.framework.auth.rbac.utils;

import com.dwei.core.mvc.repository.SimpleCacheRepository;
import com.dwei.core.utils.SpringContextUtils;
import com.dwei.domain.entity.PermissionEntity;
import com.dwei.domain.mapper.PermissionMapper;
import com.dwei.domain.repository.IPermissionRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * 权限工具类
 *
 * @author hww
 */
@Slf4j
public class PermissionUtils {

    private static final String serviceName = "permission";
    private final SimpleCacheRepository<IPermissionRepository, PermissionMapper, PermissionEntity> cacheRepository;

    private static class RoleUtilsHolder {
        private static final PermissionUtils instance = new PermissionUtils();
    }

    private static PermissionUtils getInstance() {
        return RoleUtilsHolder.instance;
    }

    private PermissionUtils() {
        cacheRepository = new SimpleCacheRepository<>(
                SpringContextUtils.getBean(IPermissionRepository.class),
                serviceName);
    }

    public static PermissionEntity get(Long permissionId) {
        return getInstance().cacheRepository.get(String.valueOf(permissionId));
    }

    /**
     * 全量刷新
     *
     * @param compel 是否强制刷新
     */
    public static synchronized void refresh(boolean compel) {
        getInstance().cacheRepository.refresh(compel);
    }

    /**
     * 指定刷新
     */
    public synchronized static void refresh(Long permissionId) {
        getInstance().cacheRepository.refresh(String.valueOf(permissionId));
    }

}
