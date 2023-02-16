package com.rodev.test.blueprint.node;

import com.rodev.test.blueprint.ChildRoot;
import org.jetbrains.annotations.Nullable;

public interface BPNode extends ChildRoot {

    void select();

    void deselect();

    boolean isNodeSelected();

    void setNodeTouchListener(NodeTouchListener listener);

    void setNodeMoveListener(NodeMoveListener listener);

    @Nullable
    BPNode getPrevious();

    @Nullable
    BPNode getNext();

    void onDelete();
}
