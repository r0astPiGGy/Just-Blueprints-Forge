package com.rodev.jbpcore.blueprint.node;

import com.rodev.jbpcore.blueprint.ChildRoot;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.PinConnectionHandler;
import com.rodev.jbpcore.blueprint.pin.PinDragListener;
import com.rodev.jbpcore.blueprint.pin.PinHoverListener;
import icyllis.modernui.view.View;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

public interface BPNode extends ChildRoot {

    void select();

    void deselect();

    boolean isNodeSelected();

    void setNodeTouchListener(NodeTouchListener listener);

    void setNodeMoveListener(NodeMoveListener listener);

    void setPinHoverListener(PinHoverListener listener);

    void setPinDragListener(PinDragListener listener);

    void setPinConnectionHandler(PinConnectionHandler pinConnectionHandler);

    void setNodePositionChangeListener(NodePositionChangeListener listener);

    void setOnNodePreDrawCallback(Function<BPNode, Boolean> callback);

    void moveTo(int x, int y);

    void addInputPin(Pin pin, String name);

    void addOutputPin(Pin pin, String name);

    default void forEachPin(Consumer<Pin> pinConsumer) {
        forEachInputPin(pinConsumer);
        forEachOutputPin(pinConsumer);
    }

    void forEachInputPin(Consumer<Pin> pinConsumer);

    void forEachOutputPin(Consumer<Pin> pinConsumer);

    void setSubtitle(String subtitle);

    // TODO: rework
    void resolveDynamicGroups();

    @NotNull
    String getType();

    int getNodeX();

    int getNodeY();

    void onDelete();

    Object serialize();

    NodeDeserializer getDeserializer(Object data);

    View asView();

    @Nullable
    Pin getFirstApplicablePinFor(Pin pin);

}
