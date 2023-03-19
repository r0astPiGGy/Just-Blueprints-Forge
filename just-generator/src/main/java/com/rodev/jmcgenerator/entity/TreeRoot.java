package com.rodev.jmcgenerator.entity;

import org.jetbrains.annotations.Nullable;

public class TreeRoot extends TreeNode {

    public TreeRoot() {
        this(null);
    }

    public TreeRoot(@Nullable TreeNode root) {
        super(root, null);
    }
}
