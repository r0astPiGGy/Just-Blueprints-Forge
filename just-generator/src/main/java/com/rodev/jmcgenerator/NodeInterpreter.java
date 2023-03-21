package com.rodev.jmcgenerator;

import com.rodev.jmcgenerator.entity.NodeEntity;
import com.rodev.jmcgenerator.entity.PinEntity;
import lombok.AccessLevel;
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

    private boolean useArgumentNames = true;

    private final List<PinEntity> selfOutputPins = new LinkedList<>();

    private Map<String, String> argsById = new HashMap<>();

    public String interpret() {
        schema = node.getRawSchema();

        fillSchema();
        fillSelfOutputPins();

        return schema;
    }

    private void fillSchema() {
        fillInput();
        fillOutput();
    }

    private void fillSelfOutputPins() {
        for (var pin : selfOutputPins) {
            pin.value = schema;
        }
    }

    private void fillInput() {
        args = new LinkedList<>();
        useArgumentNames = shouldIncludeArgumentName();
        node.data.arguments.forEach(this::addArgument);

        replacePlaceholder("args", joinArguments());

        args = null;
    }

    private String joinArguments() {
        return String.join(", ", args);
    }

    private boolean shouldIncludeArgumentName() {
        int counter = 0;

        for(var arg : node.data.arguments) {
            if(node.data.representation.shouldIgnoreArgumentById(arg.id)) continue;

            counter++;
        }

        return counter > 1;
    }

    private void addArgument(PinEntity arg) {
        var argId = arg.id;
        var value = getCodeForArgument(arg);

        // Заменяем $<arg> в схеме, если таковой есть
        replacePlaceholder(argId, value);

        argsById.put(argId, value);

        // Не добавляем в список исключенные аргументы (по типу селекторов)
        if(shouldIgnoreArgument(arg)) return;

        // Добавляем к аргументу "<name> = ", если актуальных аргументов больше одного
        if(useArgumentNames) {
            value = argId + " = " + value;
        }

        args.add(value);
    }

    private String getCodeForArgument(PinEntity arg) {
        var output = arg.data.getConnection();

        boolean connected = output != null;

        // Если аргумент подключен, то получаем значение у подключенного Pin
        if(connected) {
            var input = output.value;

            if(input == null) {
                throw new IllegalStateException("Connected value cannot be null!");
            }

            return input;
        }

        // Если аргумент не подключен и не равен null, то он является пользовательским вводом
        if (arg.value != null) {
            return onUserInputEscape(arg.value);
        }

        return "NULL";
    }

    private String onUserInputEscape(String userInput) {
        var id = UUID.randomUUID().toString();

        onUserInputEscape.accept(id, userInput);

        return id;
    }

    private boolean shouldIgnoreArgument(PinEntity arg) {
        var id = arg.id;
        var representation = node.data.representation;

        return representation.shouldIgnoreArgumentById(id);
    }

    private void fillOutput() {
        for(var output : node.data.returns) {
            fillOutputPinValue(output);

            if(output.value != null) {
                replacePlaceholder(output.id, output.value);
            }
        }
    }

    private void fillOutputPinValue(PinEntity pin) {
        var representation = pin.data.parent.data.representation;

        if(representation.output == null) {
            if(!representation.codeNeedsToBePlaced) {
                selfOutputPins.add(pin);
            }
            return;
        }

        var output = representation.output.get(pin.id);

        if(output == null) return;

        if(output.contains("$random_var_name")) {
            output = output.replace("$random_var_name", getRandomVariableName());
        }

        output = replaceArgumentsInValue(output);

        representation.output.put(pin.id, output);

        pin.value = output;
    }

    private String replaceArgumentsInValue(String value) {
        for (var argEntry : argsById.entrySet()) {
            var placeholder = "$" + argEntry.getKey();

            value = value.replace(placeholder, argEntry.getValue());
        }

        return value;
    }

    private void replacePlaceholder(String target, String replacement) {
        replaceInCode("$" + target, replacement);
    }

    private void replaceInCode(String target, String replacement) {
        schema = schema.replace(target, replacement);
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
