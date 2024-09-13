package com.dwei.domain.repository;

import com.dwei.core.mvc.repository.IBaseRepository;
import com.dwei.domain.entity.UserRoleEntity;
import com.dwei.domain.mapper.UserRoleMapper;

import java.util.List;

public interface IUserRoleRepository extends IBaseRepository<UserRoleMapper, UserRoleEntity> {

    List<UserRoleEntity> find(String userType, Long userId);

}
