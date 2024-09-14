package com.dwei.framework.auth.web.domain.request;

import com.dwei.core.mvc.pojo.request.IdRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionEnableRequest extends IdRequest {

    /**
     * 是否启用
     */
    @NotNull
    private Boolean enable;

}
