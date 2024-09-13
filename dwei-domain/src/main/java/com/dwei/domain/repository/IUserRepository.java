package com.dwei.domain.repository;

import com.dwei.core.mvc.repository.IBaseRepository;
import com.dwei.domain.entity.UserEntity;
import com.dwei.domain.mapper.UserMapper;

public interface IUserRepository extends IBaseRepository<UserMapper, UserEntity> {

}
