package com.dwei.domain.repository.impl;

import com.dwei.core.mvc.repository.BaseRepository;
import com.dwei.domain.entity.UserRoleEntity;
import com.dwei.domain.repository.IUserRoleRepository;
import com.dwei.domain.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

@Service
public class UserRoleRepository extends BaseRepository<UserRoleMapper, UserRoleEntity>
        implements IUserRoleRepository {

}




