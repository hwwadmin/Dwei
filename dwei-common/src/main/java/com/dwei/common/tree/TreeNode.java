package com.dwei.common.tree;

import cn.hutool.core.lang.Assert;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 树节点
 *
 * @author zwei
 */
@Getter
public class TreeNode<T> {

    /** 节点id */
    private final Long id;
    /** 父节点id */
    private final Long parentId;
    /** 根节点id */
    private final Long rootId;
    /** 节点内容 */
    private final T content;

    /** 父节点 */
    @Setter
    private TreeNode<T> parent;
    /** 子节点 */
    @Setter
    private List<TreeNode<T>> children;

    public TreeNode(T content, Long id, Long parentId, Long rootId) {
        Assert.notNull(content, "TreeNode#[content] is null");
        Assert.notNull(id, "TreeNode#[id] is null");
        Assert.notNull(rootId, "TreeNode#[rootId] is null");

        this.content = content;
        this.id = id;
        // 没有上级节点的时候父节点id即当前节点id
        this.parentId = Objects.nonNull(parentId) ? parentId : id;
        this.rootId = rootId;
    }

    public static <T> TreeNode<T> of(T content, Long id, Long parentId, Long rootId) {
        return new TreeNode<>(content, id, parentId, rootId);
    }

    /**
     * 获取节点等级(level)/深度(deep)
     * root level = 0
     */
    public Integer getLevel() {
        return getParentChain().size();
    }

    /**
     * 是否根节点
     */
    public boolean isRoot() {
        return Objects.equals(id, rootId);
    }

    /**
     * 是否叶节点
     */
    public boolean isLeaf() {
        return CollectionUtils.isEmpty(children);
    }

    /**
     * 获取父节点链路 (不包含自身)
     */
    public List<TreeNode<T>> getParentChain() {
        List<TreeNode<T>> result = Lists.newArrayList();
        buildParentChain(this, result);
        return result;
    }

    private static <T> void buildParentChain(TreeNode<T> treeNode, List<TreeNode<T>> result) {
        if (Objects.isNull(treeNode)) return;
        TreeNode<T> parent = treeNode.getParent();
        if (Objects.isNull(parent)) return;
        result.add(parent);
        buildParentChain(parent, result);
        Collections.reverse(result); // 颠倒一下排序，让根节点在index0
    }

    /**
     * 从顶级节点到当前节点的完整链路
     */
    public List<TreeNode<T>> getPath() {
        List<TreeNode<T>> result = getParentChain();
        result.add(this);
        return result;
    }

    /**
     * 从当前节点向下查询指定节点id返回对应节点 (包含自身)
     */
    public TreeNode<T> find(long id) {
        if (Objects.equals(this.id, id)) return this;
        if (isLeaf()) return null;
        for (TreeNode<T> child : children) {
            TreeNode<T> node = child.find(id);
            if (Objects.nonNull(node)) return node;
        }
        return null;
    }

    /**
     * 获取节点的树图根节点
     */
    public TreeNode<T> findRoot() {
        if (isRoot()) return this;
        if (Objects.isNull(parent)) return this;
        return parent.findRoot();
    }

    /**
     * 获取节点的所有树图子节点
     */
    public List<TreeNode<T>> findAllChildren() {
        List<TreeNode<T>> result = Lists.newArrayList();
        buildAllChildren(this, result);
        return result;
    }

    private static <T> void buildAllChildren(TreeNode<T> node, List<TreeNode<T>> result) {
        if (Objects.isNull(node)) return;
        if (node.isLeaf()) return;

        result.addAll(node.getChildren());
        node.getChildren().forEach(t -> buildAllChildren(t, result));
    }

    /**
     * 获取当前节点所属树图的平铺数据
     */
    public List<TreeNode<T>> flat() {
        TreeNode<T> root = findRoot();
        List<TreeNode<T>> result = Lists.newArrayList();
        result.add(root);
        result.addAll(root.findAllChildren());
        return result;
    }

}
