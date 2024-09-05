package com.dwei.common.tree;

import cn.hutool.core.lang.Assert;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 树图工具类
 *
 * @author hww
 */
public class TreeUtils {

    private TreeUtils() {

    }

    /**
     * 构造树
     * 返回的是数据集合构造成树后的指定根节点
     */
    public static <T> TreeNode<T> tree(List<TreeNode<T>> data, Long rootId) {
        Assert.notNull(rootId, "[rootId] is null");
        List<TreeNode<T>> treeList = tree(data);
        if (CollectionUtils.isEmpty(treeList)) return null;
        return treeList.stream()
                .filter(t -> Objects.equals(t.getId(), rootId))
                .findFirst()
                .orElse(null);
    }

    /**
     * 构造树
     * 返回的是数据集合构造成树后的根节点列表
     */
    public static <T> List<TreeNode<T>> tree(List<TreeNode<T>> data) {
        if (CollectionUtils.isEmpty(data)) return null;
        check(data);
        List<TreeNode<T>> rootNodes = data.stream().filter(TreeNode::isRoot).collect(Collectors.toList());
        rootNodes.forEach(rootNode -> build(data, rootNode));
        return rootNodes;
    }

    /**
     * 递归组织构造树图关系
     */
    private static <T> void build(List<TreeNode<T>> data, TreeNode<T> node) {
        if (CollectionUtils.isEmpty(data)) return;
        if (Objects.isNull(node)) return;

        List<TreeNode<T>> childNodes = data
                .stream()
                .filter(t -> Objects.equals(t.getParentId(), node.getId())
                        && !Objects.equals(t.getId(), node.getId()))
                .peek(t -> t.setParent(node))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(childNodes)) return;
        node.setChildren(childNodes);
        childNodes.forEach(t -> build(data, t));
    }

    /**
     * 校验数据集合是否合规
     */
    private static <T> void check(List<TreeNode<T>> data) {
        // 校验是否有重复的id
        Set<Long> ids = data.stream().map(TreeNode::getId).collect(Collectors.toSet());
        Assert.isTrue(Objects.equals(ids.size(), data.size()), "数据集错误，含有相同id的节点");

        // todo...
    }

    /**
     * 树图映射转换
     */
    public static <T, S extends TreeVO<S>> S map(TreeNode<T> tree, Class<S> clazz, Function<TreeNode<T>, S> convertFun) {
        S vo = convertFun.apply(tree);
        vo.setId(tree.getId());
        if (!tree.isLeaf()) tree.getChildren().forEach(t -> vo.setChildren(map(t, clazz, convertFun)));
        return vo;
    }

}
