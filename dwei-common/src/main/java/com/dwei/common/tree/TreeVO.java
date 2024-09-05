package com.dwei.common.tree;

import com.dwei.common.utils.Lists;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

/**
 * 树图抽象视图 VO
 *
 * @author hww
 */
@Getter
public abstract class TreeVO<T> {

    private Long id;
    private List<T> children;

    public void setId(long id) {
        this.id = id;
    }

    public void setChildren(T child) {
        if (Objects.isNull(this.children)) this.children = Lists.of();
        this.children.add(child);
    }

}
