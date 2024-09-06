package com.dwei.domain.repository.impl;

import com.dwei.core.mvc.repository.BaseRepository;
import com.dwei.domain.entity.RoleEntity;
import com.dwei.domain.repository.IRoleRepository;
import com.dwei.domain.mapper.RoleMapper;
import org.springframework.stereotype.Service;

@Service
public class RoleRepository extends BaseRepository<RoleMapper, RoleEntity>
        implements IRoleRepository {

}




