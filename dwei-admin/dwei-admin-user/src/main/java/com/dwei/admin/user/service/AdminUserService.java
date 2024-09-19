package com.dwei.admin.user.service;

import com.dwei.admin.user.AdminUserConstants;
import com.dwei.admin.user.domain.request.UserBindRoleRequest;
import com.dwei.admin.user.domain.request.UserLoginRequest;
import com.dwei.admin.user.domain.request.UserRegisterRequest;
import com.dwei.admin.user.domain.response.TokenResponse;
import com.dwei.admin.user.domain.response.UserInfoResponse;
import com.dwei.common.utils.Assert;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.utils.MessageUtils;
import com.dwei.core.mvc.password.PasswordEncoder;
import com.dwei.domain.entity.UserEntity;
import com.dwei.domain.repository.IUserRepository;
import com.dwei.framework.auth.AuthUtils;
import com.dwei.framework.auth.web.service.RbacService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RbacService rbacService;

    public TokenResponse login(UserLoginRequest request) {
        var user = userRepository.lambdaQuery()
                .eq(UserEntity::getUsername, request.getUsername())
                .one();
        Assert.nonNull(user);
        Assert.isTrue(passwordEncoder.checkPw(request.getPassword(), user.getPassword()), MessageUtils.message("user.login.error"));

        var token = AuthUtils.getTokenApi().createToken(AdminUserConstants.USER_TYPE, user.getId());

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
        var user = UserEntity.builder()
                .username(request.getUsername())
                .password(pwd)
                .phone(request.getPhone())
                .sex(request.getSex())
                .build();

        userRepository.save(user);
    }

    public UserInfoResponse userInfo() {
        return toResponse(userRepository.getEx(AuthUtils.getUserId()));
    }

    public void bindRole(UserBindRoleRequest request) {
        rbacService.userBindRole(AdminUserConstants.USER_TYPE, request.getUserId(), request.getRoleId());
    }

    public void unBindRole(UserBindRoleRequest request) {
        rbacService.userUnBindRole(AdminUserConstants.USER_TYPE, request.getUserId(), request.getRoleId());
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
