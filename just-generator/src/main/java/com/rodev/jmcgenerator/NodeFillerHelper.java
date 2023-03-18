package com.rodev.jmcgenerator;

import com.rodev.jmcgenerator.entity.NodeEntity;

import java.util.*;

public class NodeFillerHelper {
    private int variableNameCounter = 0;
    private final Map<String, String> userInput = new HashMap<>();

    public String replaceUserInputIn(String code) {
        for(var pair : userInput.entrySet()) {
            code = code.replace(pair.getKey(), pair.getValue());
        }

        return code;
    }

    public String getCode(NodeEntity node) {
        if(node.data.cachedCode == null) {
            var code = getCodeInternal(node);

            node.data.cachedCode = code;

            return code;
        }

        return node.data.cachedCode;
    }

    private String getCodeInternal(NodeEntity targetNode) {
        var interpreter =
                NodeInterpreter.builder(targetNode)
                        .onUserInputEscape(userInput::put)
                        .randomVariableNameSupplier(this::getRandomVariableName)
                        .build();

        var code = interpreter.interpret();

        // TODO: fix infinite recursion
        for(var pinEntry : interpreter.getCallbackPins().entrySet()) {
            var id = pinEntry.getKey();
            var pin = pinEntry.getValue();
            var node = pin.data.parent;

            if(node == targetNode) continue;

            //if(!node.data.codeGenerated)
            code = code.replace(id, getCode(node));
        }

        for(var nodeEntry : interpreter.getCallbackNodes().entrySet()) {
            var id = nodeEntry.getKey();
            var node = nodeEntry.getValue();

            code = code.replace(id, getCode(node));
        }

        return code;
    }

    private String getRandomVariableName() {
        variableNameCounter++;

        return "generated_var" + variableNameCounter;
    }

}
