package com.rodev.test.blueprint;

import com.rodev.test.blueprint.node.BPNode;
import com.rodev.test.blueprint.node.NodeBuilder;
import com.rodev.test.blueprint.node.VarGetterNode;
import com.rodev.test.blueprint.pin.*;
import com.rodev.test.blueprint.pin.exec_pin.ExecPin;
import com.rodev.test.blueprint.pin.var_pin.VarPin;
import icyllis.modernui.view.View;

import java.util.function.Consumer;

import static com.rodev.test.Colors.*;

public enum ContextActionType {

    ON_START_EVENT("Startup Event"){
        @Override
        public View createNode(int nodeColor, Consumer<Pin> onPinCreated, Consumer<BPNode> onNodeCreated) {
//            var inputExec = ExecPin.inputPin();
//            onPinCreated.accept(inputExec);

            var outputExec = ExecPin.outputPin();
            onPinCreated.accept(outputExec);

            var output = VarPin.outputPin(BLUE);
            onPinCreated.accept(output);

            var node = NodeBuilder.builder("Event onStartup")
                    .addPin(outputExec, "")
                    .addPin(output, "Startup string")
                    .build();

            onNodeCreated.accept(node);

            return node;
        }
    },
    PRINT_STRING_FUNCTION("Print string function"){
        @Override
        public View createNode(int nodeColor, Consumer<Pin> onPinCreated, Consumer<BPNode> onNodeCreated)  {
            var inputExec = ExecPin.inputPin();
            onPinCreated.accept(inputExec);

            var outputExec = ExecPin.outputPin();
            onPinCreated.accept(outputExec);

            var output = VarPin.outputPin(YELLOW);
            onPinCreated.accept(output);

            var stringInput = VarPin.inputPin(BLUE);
            onPinCreated.accept(stringInput);

            var isDebug = VarPin.inputPin(RED);
            onPinCreated.accept(isDebug);

            var node = NodeBuilder.builder("Print string")
                    .addPin(outputExec, "")
                    .addPin(inputExec, "")
                    //.addPin(output, "Return value")
                    .addPin(stringInput, "String to print")
                    .addPin(isDebug, "isDebug")
                    .build();

            onNodeCreated.accept(node);

            return node;
        }
    },
    VARIABLE_SETTER("Variable setter") {
        @Override
        public View createNode(int nodeColor, Consumer<Pin> onPinCreated, Consumer<BPNode> onNodeCreated)  {
            return null;
        }
    },
    VARIABLE_GETTER("Variable getter") {
        @Override
        public View createNode(int pinColor, Consumer<Pin> onPinCreated, Consumer<BPNode> onNodeCreated) {
            var pin = VarPin.outputPin(pinColor);
            onPinCreated.accept(pin);

            var node = new VarGetterNode(pin, "Variable");
            onNodeCreated.accept(node);

            return node;
        }
    };

    public final String contextMenuLabel;

    ContextActionType(String contextMenuLabel) {
        this.contextMenuLabel = contextMenuLabel;
    }

    public abstract View createNode(int nodeColor, Consumer<Pin> onPinCreated, Consumer<BPNode> onNodeCreated);
}
