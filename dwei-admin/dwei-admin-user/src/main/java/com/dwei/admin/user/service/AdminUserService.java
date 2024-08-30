package com.dwei.admin.user.service;

import com.dwei.admin.user.domain.response.TokenResponse;
import com.dwei.domain.entity.UserEntity;
import com.dwei.domain.repository.IUserRepository;
import com.dwei.admin.user.domain.request.UserLoginRequest;
import com.dwei.common.utils.Assert;
import com.dwei.core.mvc.password.PasswordEncoder;
import com.dwei.framework.auth.AuthUtils;
import com.dwei.framework.auth.token.Token;
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

}
