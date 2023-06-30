package com.rodev.jbpcore.workspace.impl;

import com.rodev.jbpcore.blueprint.BlueprintReference;
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
    private final BlueprintReference blueprint;
    @Setter
    private long lastOpenDate;
}
