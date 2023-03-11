package com.rodev.test.workspace;

import com.rodev.test.blueprint.graph.GraphController;
import com.rodev.test.blueprint.graph.GraphLayout;
import com.rodev.test.blueprint.node.BPNode;

import java.util.Collection;
import java.util.List;

public interface Blueprint {

    void save(Collection<BPNode> nodes);

    void loadTo(GraphController graphController);

}
