package com.rodev.jmcgenerator;

import com.rodev.jmcgenerator.entity.NodeEntity;
import com.rodev.jmcgenerator.entity.PinEntity;
import com.rodev.jmcgenerator.entity.PlaceAt;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NodeInterpreter {

    private final NodeEntity node;

    private final Supplier<String> randomVariableNameSupplier;
    private final BiConsumer<String, String> onUserInputEscape;

    private String schema;
    private List<String> args;

    @Getter
    private final Map<String, PinEntity> callbackPins = new HashMap<>();
    @Getter
    private final Map<String, NodeEntity> callbackNodes = new HashMap<>();

    private boolean includeArgumentName = true;

    public String interpret() {
        schema = node.getRawSchema();

        node.data.codeGenerated = true;

        fillSchema();

        return schema;
    }

    private void fillSchema() {
        fillInput();
        fillOutput();

        // SRP violation
        insertOutputNodes();
    }

    private void fillInput() {
        args = new LinkedList<>();
        includeArgumentName = shouldIncludeArgumentName();
        node.data.arguments.forEach(this::addArgument);

        var joinedArguments = String.join(", ", args);
        schema = schema.replace("$args", joinedArguments);

        args = null;
    }

    private boolean shouldIncludeArgumentName() {
        int counter = 0;

        for(var arg : node.data.arguments) {
            if(node.data.representation.shouldIgnoreArgumentById(arg.id)) continue;

            counter++;
        }

        return counter > 1;
    }

    private void addArgument(PinEntity inputPin) {
        var argId = inputPin.id;
        var value = inputPin.value;

        var inputConnection = inputPin.data.getConnection();

        boolean userInput = value != null;
        boolean shouldNotIgnoreArgument = !node.data.representation.shouldIgnoreArgumentById(argId);

        if(inputConnection != null) {
            fillOutputPinValue(inputConnection);

            var input = inputConnection.value;

            if(input == null) {
                input = "$[" + UUID.randomUUID() + "]";

                callbackPins.put(input, inputPin);
            }

            value = input;
            userInput = false;
        }

        if (userInput) {
            var uuid = UUID.randomUUID().toString();

            onUserInputEscape.accept(uuid, value);

            value = uuid;
        }

        if(shouldNotIgnoreArgument){
            var tempValue = value;

            if(includeArgumentName) {
                tempValue = argId + " = " + tempValue;
            }

            args.add(tempValue);
        }

        if(value == null) {
            value = "NULL";
        }

        schema = schema.replace("$" + argId, value);
    }

    private void fillOutput() {
        for(var output : node.data.returns) {
            fillOutputPinValue(output);

            if(output.value != null) {
                schema = schema.replace("$" + output.id, output.value);
            }
        }
    }

    private void insertOutputNodes() {
        for(var outputExec : node.data.outputExecPins) {
            var conId = "$" + outputExec.id;
            for(var con : outputExec.data.getConnections()) {
                var nextNode = con.data.parent;

                //if(nextNode.data.codeGenerated) continue;

                var code = nextNode.data.localId.toString();

                if(nextNode.placeAt() == PlaceAt.BEFORE) {
                    schema = code + "\n" + schema;
                } else if(schema.contains(conId)) {
                    schema = schema.replace(conId, code);
                } else {
                    schema += "\n" + code;
                }

                callbackNodes.put(code, nextNode);
            }
            schema = schema.replace(conId, "");
        }
    }

    private void fillOutputPinValue(PinEntity pin) {
        var representation = pin.data.parent.data.representation;

        if(representation.output == null) return;

        var output = representation.output.get(pin.id);

        if(output == null) return;

        if(output.contains("$random_var_name")) {
            output = output.replace("$random_var_name", getRandomVariableName());
        }

        representation.output.put(pin.id, output);

        pin.value = output;
    }

    private String getRandomVariableName() {
        return randomVariableNameSupplier.get();
    }

    public static Builder builder(@NotNull NodeEntity node) {
        return new Builder(node);
    }

    @RequiredArgsConstructor
    public static class Builder {
        private final NodeEntity node;
        private Supplier<String> randomVariableNameSupplier = () -> UUID.randomUUID().toString().replace("-", "");
        private BiConsumer<String, String> onUserInputEscape = (uuid, input) -> {};

        public Builder randomVariableNameSupplier(Supplier<String> randomVariableNameSupplier) {
            this.randomVariableNameSupplier = randomVariableNameSupplier;
            return this;
        }

        public Builder onUserInputEscape(BiConsumer<String, String> onUserInputEscape) {
            this.onUserInputEscape = onUserInputEscape;
            return this;
        }

        public NodeInterpreter build() {
            return new NodeInterpreter(node, randomVariableNameSupplier, onUserInputEscape);
        }
    }
}
