package com.dwei.framework.auth.web.domain.response;

import com.dwei.domain.entity.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse {

    private Long id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色标识
     */
    private String tag;

    /**
     * 是否启用
     */
    private Boolean enable;

    public static RoleResponse convert(RoleEntity entity) {
        return RoleResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .tag(entity.getTag())
                .enable(entity.getEnable())
                .build();
    }

}
