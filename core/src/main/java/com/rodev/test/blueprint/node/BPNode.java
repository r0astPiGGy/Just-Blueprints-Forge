package com.rodev.test.blueprint.node;

import com.rodev.test.blueprint.ChildRoot;
import com.rodev.test.blueprint.pin.Pin;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface BPNode extends ChildRoot {

    void select();

    void deselect();

    boolean isNodeSelected();

    void setNodeTouchListener(NodeTouchListener listener);

    void setNodeMoveListener(NodeMoveListener listener);

    void addInputPin(Pin pin, String name);

    void addOutputPin(Pin pin, String name);

    void forEachPin(Consumer<Pin> pinConsumer);

    @Nullable
    BPNode getPrevious();

    @Nullable
    BPNode getNext();

    void onDelete();
}
