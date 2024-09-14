package com.dwei.framework.auth.web.service;

import com.dwei.core.mvc.pojo.response.PageResponse;
import com.dwei.domain.entity.RoleEntity;
import com.dwei.domain.repository.IRoleRepository;
import com.dwei.framework.auth.web.domain.request.RoleAddRequest;
import com.dwei.framework.auth.web.domain.request.RolePageRequest;
import com.dwei.framework.auth.web.domain.response.RoleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleService {

    private final IRoleRepository roleRepository;

    public PageResponse<RoleResponse> page(RolePageRequest request) {
        var page = roleRepository.autoPage(request);
        return PageResponse.of(page, RoleResponse::convert);
    }

    public void add(RoleAddRequest request) {
        var entity = RoleEntity.builder()
                .name(request.getName())
                .tag(request.getTag())
                .enable(true)
                .build();
        roleRepository.save(entity);
    }

}
