package com.dwei.domain.repository;

import com.dwei.domain.entity.UserEntity;
import com.dwei.domain.mapper.UserMapper;
import com.dwei.core.mvc.repository.IBaseRepository;

public interface IUserRepository extends IBaseRepository<UserMapper, UserEntity> {

}
