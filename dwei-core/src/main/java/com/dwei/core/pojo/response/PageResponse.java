package com.dwei.core.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * 分页视图
 *
 * @author hww
 */
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public final class PageResponse<T> {

    /** 总页数 */
    private long total;
    /** 列表数据 */
    private List<T> items;

}
