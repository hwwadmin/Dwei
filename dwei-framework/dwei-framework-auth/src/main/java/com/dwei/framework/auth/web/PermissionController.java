package com.dwei.framework.auth.web;

import com.dwei.framework.auth.web.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
