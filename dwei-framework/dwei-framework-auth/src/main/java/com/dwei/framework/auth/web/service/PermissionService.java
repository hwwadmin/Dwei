package com.dwei.framework.auth.web.service;

import com.dwei.core.mvc.pojo.response.PageResponse;
import com.dwei.domain.entity.PermissionEntity;
import com.dwei.domain.repository.IPermissionRepository;
import com.dwei.framework.auth.rbac.utils.PermissionUtils;
import com.dwei.framework.auth.web.domain.request.PermissionAddRequest;
import com.dwei.framework.auth.web.domain.request.PermissionEnableRequest;
import com.dwei.framework.auth.web.domain.request.PermissionPageRequest;
import com.dwei.framework.auth.web.domain.response.PermissionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PermissionService {

    private final IPermissionRepository permissionRepository;

    public PageResponse<PermissionResponse> page(PermissionPageRequest request) {
        var page = permissionRepository.page(request);
        return PageResponse.of(page, PermissionResponse::convert);
    }

    public void add(PermissionAddRequest request) {
        var entity = PermissionEntity.builder()
                .name(request.getName())
                .tag(request.getTag())
                .path(request.getPath())
                .method(request.getMethod())
                .enable(true)
                .build();
        permissionRepository.save(entity);
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void enable(PermissionEnableRequest request) {
        var entity = permissionRepository.getEx(request.getId());
        entity.setEnable(request.getEnable());
        permissionRepository.save(entity);
        PermissionUtils.refresh(entity.getId());
    }

}
