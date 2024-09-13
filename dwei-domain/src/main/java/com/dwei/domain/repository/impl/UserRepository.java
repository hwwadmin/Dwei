package com.dwei.domain.repository.impl;

import com.dwei.core.mvc.repository.BaseRepository;
import com.dwei.domain.entity.UserEntity;
import com.dwei.domain.mapper.UserMapper;
import com.dwei.domain.repository.IUserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserRepository extends BaseRepository<UserMapper, UserEntity>
        implements IUserRepository {

}




