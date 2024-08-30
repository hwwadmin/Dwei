package com.dwei.admin.user;

import com.dwei.admin.user.domain.request.UserLoginRequest;
import com.dwei.admin.user.domain.response.TokenResponse;
import com.dwei.admin.user.domain.response.UserInfoResponse;
import com.dwei.admin.user.service.AdminUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员用户 Controller
 *
 * @author hww
 */
@RestController
@AllArgsConstructor
@RequestMapping("/dwei/admin/user")
public class AdminUserController {

    private final AdminUserService adminUserService;

    /**
     * 登录
     */
    @PostMapping("/login")
    public TokenResponse login(@RequestBody UserLoginRequest request) {
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
     * 用户信息
     */
    @GetMapping("/userInfo")
    public UserInfoResponse userInfo() {
        return adminUserService.userInfo();
    }

}
