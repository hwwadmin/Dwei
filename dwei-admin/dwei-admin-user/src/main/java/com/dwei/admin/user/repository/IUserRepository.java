package com.dwei.admin.user.repository;

import com.dwei.admin.user.domain.User;
import com.dwei.admin.user.mapper.UserMapper;
import com.dwei.core.mvc.repository.IBaseRepository;

public interface IUserRepository extends IBaseRepository<UserMapper, User> {

}
