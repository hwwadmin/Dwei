package com.dwei.admin.user;

import com.dwei.admin.user.domain.request.UserLoginRequest;
import com.dwei.admin.user.domain.request.UserRegisterRequest;
import com.dwei.admin.user.domain.response.TokenResponse;
import com.dwei.admin.user.domain.response.UserInfoResponse;
import com.dwei.admin.user.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员用户 Controller
 *
 * @author hww
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/user")
public class AdminUserController {

    private final AdminUserService adminUserService;

    /**
     * 登录
     */
    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody UserLoginRequest request) {
        return adminUserService.login(request);
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public void logout() {
        adminUserService.logout();
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public void register(@Valid @RequestBody UserRegisterRequest request) {
        adminUserService.register(request);
    }

    /**
     * 用户信息
     */
    @GetMapping("/userInfo")
    public UserInfoResponse userInfo() {
        return adminUserService.userInfo();
    }

}
