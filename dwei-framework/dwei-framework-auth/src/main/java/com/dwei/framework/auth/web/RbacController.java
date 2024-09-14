package com.dwei.framework.auth.web;

import com.dwei.framework.auth.web.domain.request.RbacRoleBindPermissionRequest;
import com.dwei.framework.auth.web.domain.request.RbacUserBindRoleRequest;
import com.dwei.framework.auth.web.domain.response.PermissionResponse;
import com.dwei.framework.auth.web.domain.response.RoleResponse;
import com.dwei.framework.auth.web.service.RbacService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 根据用户类型和用户id查询角色列表
     * 通用接口，使用该接口的话需要前端明确了解用户类型
     * 建议使用对应类型用户模块提供的相应接口隔离前端感知用户类型
     */
    @GetMapping("/role/{type}/{userId}")
    public List<RoleResponse> listRole(@PathVariable String type, @PathVariable Long userId) {
        return rbacService.listRole(type, userId);
    }

    /**
     * 查询角色的权限列表
     */
    @GetMapping("/permission/{roleId}")
    public List<PermissionResponse> listPermission(@PathVariable Long roleId) {
        return rbacService.listPermission(roleId);
    }

    /**
     * 用户绑定角色
     * 通用接口，使用该接口的话需要前端明确了解用户类型
     * 建议使用对应类型用户模块提供的相应接口隔离前端感知用户类型
     */
    @PostMapping("/bind-role")
    public void userBindRole(@Valid @RequestBody RbacUserBindRoleRequest request) {
        rbacService.userBindRole(request.getUserType(), request.getUserId(), request.getRoleId());
    }

    /**
     * 用户解绑角色
     * 通用接口，使用该接口的话需要前端明确了解用户类型
     * 建议使用对应类型用户模块提供的相应接口隔离前端感知用户类型
     */
    @PostMapping("/unBind-role")
    public void userUnBindRole(@Valid @RequestBody RbacUserBindRoleRequest request) {
        rbacService.userUnBindRole(request.getUserType(), request.getUserId(), request.getRoleId());
    }

    /**
     * 角色绑定权限
     */
    @PostMapping("/bind-permission")
    public void roleBindPermission(@Valid @RequestBody RbacRoleBindPermissionRequest request) {
        rbacService.roleBindPermission(request.getRoleId(), request.getPermissionId());
    }

    /**
     * 角色解绑权限
     */
    @PostMapping("/unBind-permission")
    public void roleUnBindPermission(@Valid @RequestBody RbacRoleBindPermissionRequest request) {
        rbacService.roleUnBindPermission(request.getRoleId(), request.getPermissionId());
    }

}
