package com.dwei.domain.repository.impl;

import com.dwei.domain.entity.UserEntity;
import com.dwei.domain.mapper.UserMapper;
import com.dwei.domain.repository.IUserRepository;
import com.dwei.core.mvc.repository.BaseRepositoryImpl;
import org.springframework.stereotype.Service;

@Service
public class UserRepositoryImpl extends BaseRepositoryImpl<UserMapper, UserEntity>
        implements IUserRepository {

}




