package com.dwei.framework.auth.web.domain.request;

import com.dwei.core.mvc.condition.annotation.QueryCondition;
import com.dwei.core.mvc.condition.info.QueryType;
import lombok.Data;

@Data
public class RolePageRequest {

    @QueryCondition
    private Long id;

    /**
     * 角色名称
     */
    @QueryCondition(QueryType.LIKE)
    private String name;

    /**
     * 是否启用
     */
    @QueryCondition
    private Boolean enable;

}
