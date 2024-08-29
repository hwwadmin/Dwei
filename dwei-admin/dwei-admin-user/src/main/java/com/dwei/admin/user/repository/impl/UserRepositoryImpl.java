package com.dwei.admin.user.repository.impl;

import com.dwei.admin.user.domain.User;
import com.dwei.admin.user.mapper.UserMapper;
import com.dwei.admin.user.repository.IUserRepository;
import com.dwei.core.mvc.repository.BaseRepositoryImpl;
import org.springframework.stereotype.Service;

@Service
public class UserRepositoryImpl extends BaseRepositoryImpl<UserMapper, User>
        implements IUserRepository {

}




