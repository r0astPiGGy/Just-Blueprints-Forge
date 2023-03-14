package com.rodev.jbpcore.workspace.impl;

import com.rodev.jbpcore.blueprint.graph.GraphController;
import com.rodev.jbpcore.blueprint.node.BPNode;
import com.rodev.jbpcore.workspace.Blueprint;
import com.rodev.jbpcore.workspace.Project;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.util.Collection;

@Getter
@RequiredArgsConstructor
public abstract class ProjectImpl implements Project {

    private final String name;
    private final File directory;
    private final long createdDate;
    @Setter
    private transient long lastOpenDate;

    @Override
    public Blueprint getBlueprint() {
        return new BlueprintImpl();
    }

    abstract
    protected void onBlueprintSave(Collection<BPNode> nodes);

    abstract
    protected void onBlueprintLoad(GraphController graphController);

    abstract
    protected void onBlueprintCompile();

    private class BlueprintImpl implements Blueprint {

        @Override
        public void save(Collection<BPNode> nodes) {
            onBlueprintSave(nodes);
        }

        @Override
        public void loadTo(GraphController graphController) {
            onBlueprintLoad(graphController);
        }

        @Override
        public void compile() {
            onBlueprintCompile();
        }
    }
}
