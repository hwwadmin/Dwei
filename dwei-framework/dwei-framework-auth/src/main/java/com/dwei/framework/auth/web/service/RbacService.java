package com.dwei.framework.auth.web.service;

import com.dwei.common.utils.Assert;
import com.dwei.domain.entity.UserRoleEntity;
import com.dwei.domain.repository.IUserRoleRepository;
import com.dwei.framework.auth.rbac.utils.UserRoleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RbacService {

    private final IUserRoleRepository userRoleRepository;

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void userBindRole(String userType, Long userId, Long roleId) {
        Assert.isStrNotBlank(userType);
        Assert.nonNull(userId);
        Assert.nonNull(roleId);

        var entity = UserRoleEntity.builder()
                .userType(userType)
                .userId(userId)
                .roleId(roleId)
                .build();
        userRoleRepository.save(entity);

        UserRoleUtils.refresh(userType, userId);
    }

}
