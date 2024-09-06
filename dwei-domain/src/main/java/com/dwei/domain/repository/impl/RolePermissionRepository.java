package com.dwei.domain.repository.impl;

import com.dwei.core.mvc.repository.BaseRepository;
import com.dwei.domain.entity.RolePermissionEntity;
import com.dwei.domain.repository.IRolePermissionRepository;
import com.dwei.domain.mapper.RolePermissionMapper;
import org.springframework.stereotype.Service;

@Service
public class RolePermissionRepository extends BaseRepository<RolePermissionMapper, RolePermissionEntity>
        implements IRolePermissionRepository {

}




