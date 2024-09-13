package com.dwei.framework.auth.web;

import com.dwei.framework.auth.web.domain.request.RbacUserBindRoleRequest;
import com.dwei.framework.auth.web.service.RbacService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RBAC权限管理 Controller
 *
 * @author hww
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/rbac")
public class RbacController {

    private final RbacService rbacService;

    /**
     * 用户绑定角色
     * 通用接口，使用该接口的话，需要前端明确了解用户类型
     * 建议通过对应类型用户模块提供的接口来给对应类型用户绑定角色
     */
    @PostMapping("/bind-role")
    public void userBindRole(@Valid @RequestBody RbacUserBindRoleRequest request) {
        rbacService.userBindRole(request.getUserType(), request.getUserId(), request.getRoleId());
    }

}
