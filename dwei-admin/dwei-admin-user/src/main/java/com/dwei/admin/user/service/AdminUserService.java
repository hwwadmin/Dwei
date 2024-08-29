package com.dwei.admin.user.service;

import com.dwei.admin.user.domain.User;
import com.dwei.admin.user.repository.IUserRepository;
import com.dwei.admin.user.request.UserLoginRequest;
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

    public Token login(UserLoginRequest request) {
        var user = userRepository.lambdaQuery()
                .eq(User::getUsername, request.getUsername())
                .one();
        Assert.nonNull(user);
        Assert.isTrue(passwordEncoder.checkPw(user.getPassword(), request.getPassword()));
        return AuthUtils.getTokenApi().createToken(user.getId());
    }

}
