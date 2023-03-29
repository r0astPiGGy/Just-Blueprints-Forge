package com.rodev.jbpcore.blueprint.pin;

import com.rodev.jbpcore.blueprint.node.BPNode;
import org.jetbrains.annotations.NotNull;

public interface Dynamic {

    boolean isDynamic();

    @NotNull
    Pin findDependantInNode(BPNode node);

}
