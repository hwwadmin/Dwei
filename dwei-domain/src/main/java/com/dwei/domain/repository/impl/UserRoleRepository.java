package com.dwei.domain.repository.impl;

import com.dwei.common.utils.Assert;
import com.dwei.common.utils.Lists;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.mvc.repository.BaseRepository;
import com.dwei.domain.entity.UserRoleEntity;
import com.dwei.domain.mapper.UserRoleMapper;
import com.dwei.domain.repository.IUserRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleRepository extends BaseRepository<UserRoleMapper, UserRoleEntity>
        implements IUserRoleRepository {

    @Override
    public List<UserRoleEntity> find(String userType, Long userId) {
        Assert.isStrNotBlank(userType);
        Assert.nonNull(userId);
        var result = lambdaQuery()
                .eq(UserRoleEntity::getUserType, userType)
                .eq(UserRoleEntity::getUserId, userId)
                .isNull(UserRoleEntity::getDeleteTime)
                .list();
        return ObjectUtils.nonNull(result) ? result : Lists.of();
    }

}




