package com.rodev.jbpcore.workspace;

import com.rodev.jbpcore.blueprint.graph.GraphController;
import com.rodev.jbpcore.blueprint.node.BPNode;
import com.rodev.jbpcore.workspace.compiler.CodeCompiler;

import java.util.Collection;

public interface Blueprint {

    void save(Collection<BPNode> nodes);

    void loadTo(GraphController graphController);

    void compile(CodeCompiler.CompileMode compileMode);

}
