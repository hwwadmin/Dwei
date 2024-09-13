package com.dwei.domain.repository.impl;

import com.dwei.core.mvc.repository.BaseRepository;
import com.dwei.domain.entity.PermissionEntity;
import com.dwei.domain.mapper.PermissionMapper;
import com.dwei.domain.repository.IPermissionRepository;
import org.springframework.stereotype.Service;

@Service
public class PermissionRepository extends BaseRepository<PermissionMapper, PermissionEntity>
        implements IPermissionRepository {

}




