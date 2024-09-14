package com.dwei.framework.auth.rbac.utils;

import com.dwei.common.constants.AppConstants;
import com.dwei.common.pojo.Pair;
import com.dwei.common.utils.Assert;
import com.dwei.common.utils.Lists;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.mvc.repository.ListCacheRepository;
import com.dwei.core.utils.SpringContextUtils;
import com.dwei.domain.entity.RoleEntity;
import com.dwei.domain.entity.UserRoleEntity;
import com.dwei.domain.mapper.UserRoleMapper;
import com.dwei.domain.repository.IUserRoleRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户角色工具类
 *
 * @author hww
 */
public class UserRoleUtils {

    private static final String serviceName = "user2Role";
    private final ListCacheRepository<IUserRoleRepository, UserRoleMapper, UserRoleEntity> cacheRepository;

    private static class UserRoleUtilsHolder {
        private static final UserRoleUtils instance = new UserRoleUtils();
    }

    private static UserRoleUtils getInstance() {
        return UserRoleUtilsHolder.instance;
    }

    private UserRoleUtils() {
        cacheRepository = new ListCacheRepository<>(
                SpringContextUtils.getBean(IUserRoleRepository.class),
                serviceName,
                t -> buildIdKey(t.getUserType(), t.getUserId()),
                (code, repository) -> {
                    var pair = parseIdKey(code);
                    return Optional.ofNullable(repository.find(pair.getFirst(), pair.getSecond()));
                });
    }

    public static List<RoleEntity> get(String userType, Long userId) {
        var idKey = buildIdKey(userType, userId);
        var userRole = getInstance().cacheRepository.get(idKey);
        if (ObjectUtils.isNull(userRole)) return Lists.of();
        var roleIds = userRole.stream()
                .map(UserRoleEntity::getRoleId)
                .distinct()
                .toList();
        if (ObjectUtils.isNull(roleIds)) return Lists.of();

        return roleIds.stream().map(RoleUtils::get).filter(RoleEntity::getEnable).collect(Collectors.toList());
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
    public synchronized static void refresh(String userType, Long userId) {
        getInstance().cacheRepository.refresh(buildIdKey(userType, userId));
    }

    /**
     * idKey构建
     */
    public static String buildIdKey(String userType, long userId) {
        Assert.isStrNotBlank(userType, "登录类型缺失");
        return userType + AppConstants.UNDER_LINE + userId;
    }

    /**
     * idKey解析
     */
    public static Pair<String, Long> parseIdKey(String idKey) {
        var s = idKey.split(AppConstants.UNDER_LINE);
        return Pair.of(s[0], Long.valueOf(s[1]));
    }

}
