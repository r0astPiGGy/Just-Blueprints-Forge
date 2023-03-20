package com.rodev.jmcgenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodev.jmcgenerator.data.GeneratorData;
import com.rodev.jmcgenerator.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

@RequiredArgsConstructor
public class CodeGenerator {

    private final File savedBlueprint;
    private final File fileToWrite;

    @SneakyThrows
    public void generate(GeneratorData data, int indentation) {
        var objectMapper = new ObjectMapper();
        var blueprint = objectMapper.readValue(savedBlueprint, BlueprintEntity.class);

        var blueprintEntityFiller = new BlueprintEntityFiller(data);
        blueprintEntityFiller.fill(blueprint);

        var tree = new TreeRoot();
        for (var event : blueprintEntityFiller.getEvents()) {
            walkNode(event, tree);
        }

        var nodeFillerHelper = new GeneratorHelper(tree);

        var code = nodeFillerHelper.generateCode();

        code = CodeIndentation.applyIndentation(code, indentation);
        code = nodeFillerHelper.replaceUserInputIn(code);

        Files.writeString(fileToWrite.toPath(), code);

        data.recycle();
    }
    
    private void walkNode(NodeEntity node, TreeNode tree) {
        if (tree.isAdded(node)) return;

        for (var arg : node.data.arguments) {
            var connection = arg.data.getConnection();

            if (connection == null) continue;

            var connectedNode = connection.data.parent;

            walkNode(connectedNode, tree);
        }

        var innerTree = tree.add(node);
        var nodeSchema = node.getRawSchema();

        for (var outputExecPin : node.data.outputExecPins) {
            var connections = outputExecPin.data.getConnections();
            var placeholder = "$" + outputExecPin.id;

            // Если у exec pin нет соединений, то убираем плейсхолдер и идём дальше.
            if(connections.isEmpty()) {
                nodeSchema = nodeSchema.replace(placeholder, "");
                node.data.representation.schema = nodeSchema;
                continue;
            }

            boolean containsExecPin = nodeSchema.contains(placeholder);

            var localTree = tree;

            if (containsExecPin) {
                var contextId = "$[" + UUID.randomUUID() + "]";
                nodeSchema = nodeSchema.replace(placeholder, contextId);
                node.data.representation.schema = nodeSchema;
                localTree = innerTree;
                localTree.setChildContext(contextId);
            }

            for (var connection : connections) {
                walkNode(connection.data.parent, localTree);
            }
        }
    }

}
