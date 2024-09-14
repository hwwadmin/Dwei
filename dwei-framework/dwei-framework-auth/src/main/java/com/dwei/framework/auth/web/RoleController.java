package com.dwei.framework.auth.web;

import com.dwei.core.mvc.pojo.response.PageResponse;
import com.dwei.framework.auth.web.domain.request.RoleAddRequest;
import com.dwei.framework.auth.web.domain.request.RolePageRequest;
import com.dwei.framework.auth.web.domain.response.RoleResponse;
import com.dwei.framework.auth.web.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 角色管理 Controller
 *
 * @author hww
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/role")
public class RoleController {

    private final RoleService roleService;

    /**
     * 分页查询
     */
    @GetMapping
    public PageResponse<RoleResponse> page(RolePageRequest request) {
        return roleService.page(request);
    }

    /**
     * 新增
     */
    @PostMapping
    public void add(@Valid @RequestBody RoleAddRequest request) {
        roleService.add(request);
    }

}
