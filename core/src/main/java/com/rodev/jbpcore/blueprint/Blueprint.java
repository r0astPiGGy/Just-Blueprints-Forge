package com.rodev.jbpcore.blueprint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rodev.jbpcore.blueprint.graph.GraphController;
import com.rodev.jbpcore.blueprint.node.GraphNode;
import com.rodev.jbpcore.workspace.compiler.CodeCompiler;
import com.rodev.jbpcore.workspace.impl.WorkspaceImpl;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Blueprint {

    private final File file;
    private BlueprintDto blueprintDto;

    public Blueprint(File file, @NotNull BlueprintDto blueprintDto) {
        this.file = file;
        this.blueprintDto = Objects.requireNonNull(blueprintDto);
    }

    public void loadInto(GraphController graphController) {
        if (blueprintDto == null) {
            throw new IllegalStateException("This method couldn't be called twice.");
        }

        blueprintDto.loadInto(graphController);
        blueprintDto = null;
    }

    public BlueprintReference save(List<GraphNode> nodes) {
        var nodeEntities = nodes.stream().map(this::serialize).toList();
        var blueprint = new BlueprintReference.BlueprintEntity(nodeEntities);

        var objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        try {
            objectMapper.writeValue(file, blueprint);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new BlueprintReference(file);
    }

    private BlueprintReference.NodeEntity serialize(GraphNode node) {
        var entity = new BlueprintReference.NodeEntity();

        entity.id = node.getType();
        entity.position = new BlueprintReference.NodeLocation(node.getNodeX(), node.getNodeY());
        entity.data = node.serialize();

        return entity;
    }

    public void compile(CodeCompiler.CompileMode compileMode) {

    }

}
