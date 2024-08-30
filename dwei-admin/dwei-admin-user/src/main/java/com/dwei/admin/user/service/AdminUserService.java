package com.dwei.admin.user.service;

import com.dwei.admin.user.domain.request.UserRegisterRequest;
import com.dwei.admin.user.domain.response.TokenResponse;
import com.dwei.admin.user.domain.response.UserInfoResponse;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.domain.entity.UserEntity;
import com.dwei.domain.repository.IUserRepository;
import com.dwei.admin.user.domain.request.UserLoginRequest;
import com.dwei.common.utils.Assert;
import com.dwei.core.mvc.password.PasswordEncoder;
import com.dwei.framework.auth.AuthUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminUserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public TokenResponse login(UserLoginRequest request) {
        var user = userRepository.lambdaQuery()
                .eq(UserEntity::getUsername, request.getUsername())
                .one();
        Assert.nonNull(user);
        Assert.isTrue(passwordEncoder.checkPw(request.getPassword(), user.getPassword()));

        var token = AuthUtils.getTokenApi().createToken(user.getId());

        return TokenResponse.builder()
                .userId(token.getUserId())
                .token(token.getToken())
                .build();
    }

    public void logout() {
        AuthUtils.getTokenApi().logout();
    }

    public void register(UserRegisterRequest request) {
        Assert.isFalse(userRepository.lambdaQuery().eq(UserEntity::getUsername, request.getUsername()).exists());
        if (ObjectUtils.nonNull(request.getPhone()))
            Assert.isFalse(userRepository.lambdaQuery().eq(UserEntity::getPhone, request.getPhone()).exists());

        var pwd = passwordEncoder.encode(request.getPassword());
        var user = new UserEntity()
                .setUsername(request.getUsername())
                .setPassword(pwd)
                .setPhone(request.getPhone())
                .setSex(request.getSex());
        user.init();
        userRepository.save(user);
    }

    public UserInfoResponse userInfo() {
        var user = userRepository.getById(AuthUtils.getUserId());
        Assert.nonNull(user);
        return toResponse(user);
    }

    private UserInfoResponse toResponse(UserEntity user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .phone(user.getPhone())
                .email(user.getEmail())
                .name(user.getName())
                .avatar(user.getAvatar())
                .sex(user.getSex())
                .build();
    }

}
