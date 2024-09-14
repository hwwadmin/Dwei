package com.dwei.framework.auth.web;

import com.dwei.core.mvc.pojo.response.PageResponse;
import com.dwei.framework.auth.web.domain.request.PermissionAddRequest;
import com.dwei.framework.auth.web.domain.request.PermissionPageRequest;
import com.dwei.framework.auth.web.domain.response.PermissionResponse;
import com.dwei.framework.auth.web.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 权限管理 Controller
 *
 * @author hww
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/permission")
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * 分页查询
     */
    @GetMapping
    public PageResponse<PermissionResponse> page(PermissionPageRequest request) {
        return permissionService.page(request);
    }

    /**
     * 新增
     */
    @PostMapping
    public void add(@Valid @RequestBody PermissionAddRequest request) {
        permissionService.add(request);
    }

}
