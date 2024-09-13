package com.dwei.domain.repository.impl;

import com.dwei.common.utils.Assert;
import com.dwei.common.utils.Lists;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.mvc.repository.BaseRepository;
import com.dwei.domain.entity.RolePermissionEntity;
import com.dwei.domain.mapper.RolePermissionMapper;
import com.dwei.domain.repository.IRolePermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolePermissionRepository extends BaseRepository<RolePermissionMapper, RolePermissionEntity>
        implements IRolePermissionRepository {

    @Override
    public List<RolePermissionEntity> find(Long roleId) {
        Assert.nonNull(roleId);
        var data = lambdaQuery()
                .eq(RolePermissionEntity::getRoleId, roleId)
                .isNull(RolePermissionEntity::getDeleteTime)
                .list();
        return ObjectUtils.nonNull(data) ? data : Lists.of();
    }

}




