package com.dwei.core.mvc.pojo.response;

import com.dwei.common.utils.Lists;
import com.dwei.common.utils.ObjectUtils;
import com.github.pagehelper.PageInfo;
import lombok.*;

import java.util.List;
import java.util.function.Function;

/**
 * 分页视图
 *
 * @author hww
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public final class PageResponse<T> {

    /** 总页数 */
    private long total;
    /** 列表数据 */
    private List<T> items;

    public static <T, R> PageResponse<R> empty() {
        return PageResponse.<R>builder()
                .total(0L)
                .items(List.of())
                .build();
    }

    public static <T, R> PageResponse<R> of(List<T> list, Function<T, R> mapper) {
        if (ObjectUtils.isNull(list)) return empty();
        return of(list, new PageInfo<>(list).getTotal(), mapper);
    }

    public static <T, R> PageResponse<R> of(List<T> list, long total, Function<T, R> mapper) {
        if (ObjectUtils.isNull(list)) return empty();
        return PageResponse.<R>builder()
                .total(total)
                .items(Lists.map(list, mapper))
                .build();
    }

}
