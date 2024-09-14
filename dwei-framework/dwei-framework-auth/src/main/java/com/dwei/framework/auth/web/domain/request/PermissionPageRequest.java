package com.dwei.framework.auth.web.domain.request;

import com.dwei.core.mvc.condition.annotation.QueryCondition;
import com.dwei.core.mvc.condition.info.QueryType;
import lombok.Data;

@Data
public class PermissionPageRequest {

    @QueryCondition
    private Long id;

    /**
     * 资源路径
     */
    @QueryCondition(QueryType.LIKE)
    private String path;

    /**
     * 是否启用
     */
    @QueryCondition
    private Boolean enable;

}
