package com.dwei.domain.repository;

import com.dwei.core.mvc.repository.IBaseRepository;
import com.dwei.domain.entity.RolePermissionEntity;
import com.dwei.domain.mapper.RolePermissionMapper;

import java.util.List;

public interface IRolePermissionRepository extends IBaseRepository<RolePermissionMapper, RolePermissionEntity> {

    List<RolePermissionEntity> find(Long roleId);

}
