package com.dwei.core.pojo.response;

import com.dwei.core.pojo.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 主键ID视图
 *
 * @author hww
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IdResponse {

    private Long id;

    public static IdResponse of(Long id) {
        return new IdResponse(id);
    }

    public static IdResponse of(BaseEntity entity) {
        return of(entity.getId());
    }

}
