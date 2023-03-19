package com.rodev.jmcgenerator.entity;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class TreeNode {

    private final Queue<TreeNode> innerTree = new LinkedList<>();
    private final Set<String> addedNodes = new HashSet<>();

    @Nullable
    private final TreeNode root;
    @Nullable
    private final NodeEntity node;

    /**
     * Имя плейсхолдера для вставки кода. Если null, добавляется в конец текущего кода
     */
    @Setter
    @Getter
    @Nullable
    private String context;

    /**
     * Динамическое имя контекста для внутреннего дерева
     */
    @Setter
    @Nullable
    private String childContext;

    public TreeNode(NodeEntity nodeEntity) {
        this(null, nodeEntity);
    }

    public TreeNode(@Nullable TreeNode root, @Nullable NodeEntity nodeEntity) {
        this.root = root;
        this.node = nodeEntity;

        if(node == null) return;

        addId(nodeEntity);
    }

    /**
     * @return Added inner tree
     */
    public TreeNode add(NodeEntity entity) {
        requireNotAdded(entity);

        var tree = new TreeNode(this, entity);
        innerTree.add(tree);
        tree.setContext(childContext);

        addId(entity);

        return tree;
    }

    private void requireNotAdded(NodeEntity entity) {
        if(isAdded(entity)) throw new IllegalArgumentException("This node is already added.");
    }

    private void addId(NodeEntity entity) {
        var addedId = entity.getLocalId();
        findRoot().addedNodes.add(addedId);
    }

    public boolean isAdded(NodeEntity node) {
        return findRoot().addedNodes.contains(node.getLocalId());
    }

    private TreeNode findRoot() {
        if(root == null) return this;

        var root = this.root;

        while(true) {
            if(root.root == null) return root;

            root = root.root;
        }
    }

    @Nullable
    public TreeNode getParent() {
        return root;
    }

    @Nullable
    public NodeEntity getNode() {
        return node;
    }

    public void forEach(Consumer<TreeNode> treeNodeConsumer) {
        treeNodeConsumer.accept(this);

        for (var treeNode : innerTree) {
            treeNode.forEach(treeNodeConsumer);
        }
    }

    // Debug
    public void printGraph() {
        printGraph(0);
    }

    public void printGraph(int tabulation) {
        printNode(node, tabulation);
        for (var treeNode : innerTree) {
            treeNode.printGraph(tabulation + 2);
        }
    }

    private void printNode(NodeEntity node, int tabulation) {
        var info = "null";

        if(node != null) {
            info = node.id;
        }

        System.out.println(" ".repeat(tabulation) + info + ", context = " + context);
    }

}
