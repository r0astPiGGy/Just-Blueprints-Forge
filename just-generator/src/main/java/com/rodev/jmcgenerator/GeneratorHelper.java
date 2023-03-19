package com.rodev.jmcgenerator;

import com.rodev.jmcgenerator.entity.NodeEntity;
import com.rodev.jmcgenerator.entity.TreeNode;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class GeneratorHelper {

    private int variableNameCounter = 0;
    private final TreeNode tree;

    private StringBuilder codeBuilder;
    private final List<String> placeholdersToRemove = new LinkedList<>();

    private final Map<String, String> userInput = new HashMap<>();

    public String replaceUserInputIn(String code) {
        for(var pair : userInput.entrySet()) {
            code = code.replace(pair.getKey(), pair.getValue());
        }

        return code;
    }

    public String generateCode() {
        onCodeGenerate();

        return codeBuilder.toString();
    }

    private void onCodeGenerate() {
        codeBuilder = new StringBuilder();
        tree.forEach(this::onTreeWalk);
        removePlaceholders();
    }

    private void onTreeWalk(TreeNode treeNode) {
        var node = treeNode.getNode();

        if(node == null) return;

        var code = getCodeForNode(node);

        if(code == null) return;

        var context = treeNode.getContext();

        if(context == null) {
            append(code);
            return;
        }

        code += "\n" + context;
        replaceInCode(context, code);
        placeholdersToRemove.add(context);
    }

    @Nullable
    private String getCodeForNode(NodeEntity node) {
        var schema = node.getRawSchema();

        var interpreter = NodeInterpreter.builder(node)
                .onUserInputEscape(userInput::put)
                .randomVariableNameSupplier(this::getRandomVariableName)
                .build();

        schema = interpreter.interpret();

        if(!node.data.representation.codeNeedsToBePlaced)  {
            schema = null;
        }

        return schema;
    }

    private void removePlaceholders() {
        var tempCode = codeBuilder.toString();
        for(var placeholder : placeholdersToRemove) {
            tempCode = tempCode.replace(placeholder, "");
        }
        codeBuilder = new StringBuilder(tempCode);
    }

    private void replaceInCode(String target, String replacement) {
        var code = codeBuilder.toString();

        code = code.replace(target, replacement);

        codeBuilder = new StringBuilder(code);
    }

    private void append(String text) {
        codeBuilder.append("\n").append(text);
    }

    private String getRandomVariableName() {
        variableNameCounter++;

        return "generated_var" + variableNameCounter;
    }

}
