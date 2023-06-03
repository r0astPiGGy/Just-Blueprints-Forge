package com.rodev.jbpcore.workspace.impl;

import com.rodev.jbpcore.blueprint.graph.GraphController;
import com.rodev.jbpcore.blueprint.node.GraphNode;
import com.rodev.jbpcore.blueprint.Blueprint;
import com.rodev.jbpcore.workspace.Project;
import com.rodev.jbpcore.workspace.compiler.CodeCompiler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.Collection;

@Getter
@AllArgsConstructor
public abstract class ProjectImpl implements Project {

    private final String name;
    private final File directory;
    private final long createdDate;
    @Setter
    private long lastOpenDate;

    @Override
    public Blueprint getBlueprint() {
        return new BlueprintImpl();
    }

    abstract
    protected void onBlueprintSave(Collection<GraphNode> nodes);

    abstract
    protected void onBlueprintLoad(GraphController graphController);

    abstract
    protected void onBlueprintCompile(CodeCompiler.CompileMode compileMode);

    private class BlueprintImpl implements Blueprint {

        @Override
        public void save(Collection<GraphNode> nodes) {
            onBlueprintSave(nodes);
        }

        @Override
        public void loadTo(GraphController graphController) {
            onBlueprintLoad(graphController);
        }

        @Override
        public void compile(CodeCompiler.CompileMode compileMode) {
            onBlueprintCompile(compileMode);
        }
    }
}
