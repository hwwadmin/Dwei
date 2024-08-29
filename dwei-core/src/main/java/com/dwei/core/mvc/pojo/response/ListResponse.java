package com.dwei.core.mvc.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 标准的列表视图
 *
 * @author hww
 */
@Data
@AllArgsConstructor
public class ListResponse<T> {

    private T items;

}
