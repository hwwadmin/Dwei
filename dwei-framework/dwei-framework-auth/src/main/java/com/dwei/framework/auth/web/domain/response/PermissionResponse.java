package com.dwei.framework.auth.web.domain.response;

import com.dwei.domain.entity.PermissionEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionResponse {

    private Long id;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 权限标识
     */
    private String tag;

    /**
     * 资源路径
     */
    private String path;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 是否启用
     */
    private Boolean enable;

    public static PermissionResponse convert(PermissionEntity entity) {
        return PermissionResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .tag(entity.getTag())
                .path(entity.getPath())
                .method(entity.getMethod())
                .enable(entity.getEnable())
                .build();
    }

}
