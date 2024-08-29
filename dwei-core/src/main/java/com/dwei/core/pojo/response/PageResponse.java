package com.dwei.core.pojo.response;

import com.dwei.common.utils.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.function.Function;

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

    /** 页面大小 */
    private int size;
    /** 总页数 */
    private long total;
    /** 列表数据 */
    private List<T> rows;

    private PageResponse() {
    }

    public <K> PageResponse(PageResponse<K> page, List<T> rows) {
        this.size = page.getSize();
        this.total = page.getTotal();
        this.rows = rows;
    }

    public <R> PageResponse<R> map(Function<T, R> function) {
        return new PageResponse<>(this, Lists.map(getRows(), function));
    }

    public static <T> PageResponse<T> empty() {
        return new PageResponse<>();
    }

    public static <T> PageResponse<T> of(List<T> rows) {
        return new PageResponse<>(rows.size(), rows.size(), rows);
    }

}
