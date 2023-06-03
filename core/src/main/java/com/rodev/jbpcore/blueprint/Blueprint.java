package com.rodev.jbpcore.blueprint;

import com.rodev.jbpcore.blueprint.graph.GraphController;
import com.rodev.jbpcore.blueprint.node.GraphNode;
import com.rodev.jbpcore.workspace.compiler.CodeCompiler;

import java.util.Collection;

public interface Blueprint {

    void save(Collection<GraphNode> nodes);

    void loadTo(GraphController graphController);

    void compile(CodeCompiler.CompileMode compileMode);

}
