package com.dwei.framework.auth.rbac.utils;

import com.dwei.core.mvc.repository.SimpleCacheRepository;
import com.dwei.core.utils.SpringContextUtils;
import com.dwei.domain.entity.RoleEntity;
import com.dwei.domain.mapper.RoleMapper;
import com.dwei.domain.repository.IRoleRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * 角色工具类
 *
 * @author hww
 */
@Slf4j
public class RoleUtils {

    private static final String serviceName = "role";
    private final SimpleCacheRepository<IRoleRepository, RoleMapper, RoleEntity> cacheRepository;

    private static class RoleUtilsHolder {
        private static final RoleUtils instance = new RoleUtils();
    }

    private static RoleUtils getInstance() {
        return RoleUtilsHolder.instance;
    }

    private RoleUtils() {
        cacheRepository = new SimpleCacheRepository<>(
                SpringContextUtils.getBean(IRoleRepository.class),
                serviceName);
    }

    public static RoleEntity get(String key) {
        return getInstance().cacheRepository.get(key);
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
    public synchronized static void refresh(String key) {
        getInstance().cacheRepository.refresh(key);
    }

}
