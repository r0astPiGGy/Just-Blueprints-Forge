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

    BEBRA_FUNC("Bebra func"){
        @Override
        public View createNode(int nodeColor, Consumer<Pin> onPinCreated, Consumer<BPNode> onNodeCreated) {
            var inputExec = ExecPin.inputPin();
            onPinCreated.accept(inputExec);

            var falseOutputExec = ExecPin.outputPin();
            onPinCreated.accept(falseOutputExec);

            var conditionInput = VarPin.inputPin(GREEN);
            onPinCreated.accept(conditionInput);

            var locationInput = VarPin.inputPin(LOCATION);
            onPinCreated.accept(locationInput);

            var listInput = VarPin.inputPin(LIST_YELLOW);
            onPinCreated.accept(listInput);

            var numberInput = VarPin.inputPin(NUMBER_RED);
            onPinCreated.accept(numberInput);

            var textInput = VarPin.inputPin(LIGHT_BLUE);
            onPinCreated.accept(textInput);

            var soundInput = VarPin.inputPin(SOUND_COLOR);
            onPinCreated.accept(soundInput);

            var vectorInput = VarPin.inputPin(VECTOR_COLOR);
            onPinCreated.accept(vectorInput);

            var particleInput = VarPin.inputPin(PARTICLE_COLOR);
            onPinCreated.accept(particleInput);

            var potionInput = VarPin.inputPin(POTION);
            onPinCreated.accept(potionInput);

            var node = NodeBuilder.builder(POTION,"Potion")
                    .addPin(inputExec, "")
                    .addPin(conditionInput, "Variable")
                    .addPin(locationInput, "Location")
                    .addPin(listInput, "List")
                    .addPin(numberInput, "Number")
                    .addPin(textInput, "Text")
                    .addPin(potionInput, "Potion")
                    .addPin(particleInput, "Particle")
                    .addPin(vectorInput, "Vector")
                    .addPin(soundInput, "Sound")
                    .addPin(falseOutputExec, "")
                    .build();

            onNodeCreated.accept(node);

            return node;
        }
    },

    BEBRA_FUNC2("Bebra func2"){
        @Override
        public View createNode(int nodeColor, Consumer<Pin> onPinCreated, Consumer<BPNode> onNodeCreated) {
            var inputExec = ExecPin.inputPin();
            onPinCreated.accept(inputExec);

            var falseOutputExec = ExecPin.outputPin();
            onPinCreated.accept(falseOutputExec);

            var conditionInput = VarPin.inputPin(GREEN);
            onPinCreated.accept(conditionInput);

            var locationInput = VarPin.inputPin(LOCATION);
            onPinCreated.accept(locationInput);

            var listInput = VarPin.inputPin(LIST_YELLOW);
            onPinCreated.accept(listInput);

            var numberInput = VarPin.inputPin(NUMBER_RED);
            onPinCreated.accept(numberInput);

            var textInput = VarPin.inputPin(LIGHT_BLUE);
            onPinCreated.accept(textInput);

            var soundInput = VarPin.inputPin(SOUND_COLOR);
            onPinCreated.accept(soundInput);

            var vectorInput = VarPin.inputPin(VECTOR_COLOR);
            onPinCreated.accept(vectorInput);

            var particleInput = VarPin.inputPin(PARTICLE_COLOR);
            onPinCreated.accept(particleInput);

            var potionInput = VarPin.inputPin(POTION);
            onPinCreated.accept(potionInput);

            var mapInput = VarPin.inputPin(MAP);
            onPinCreated.accept(mapInput);

            var node = NodeBuilder.builder(NUMBER_RED,"Number")
                    .addPin(inputExec, "")
                    .addPin(conditionInput, "Variable")
                    .addPin(locationInput, "Location")
                    .addPin(listInput, "List")
                    .addPin(mapInput, "Map")
                    .addPin(numberInput, "Number")
                    .addPin(potionInput, "Potion")
                    .addPin(textInput, "Text")
                    .addPin(vectorInput, "Vector")
                    .addPin(particleInput, "Particle")
                    .addPin(soundInput, "Sound")
                    .addPin(falseOutputExec, "")
                    .build();

            onNodeCreated.accept(node);

            return node;
        }
    },
    BRANCH("Branch"){
        @Override
        public View createNode(int nodeColor, Consumer<Pin> onPinCreated, Consumer<BPNode> onNodeCreated) {
            var inputExec = ExecPin.inputPin();
            onPinCreated.accept(inputExec);

            var trueOutputExec = ExecPin.outputPin();
            onPinCreated.accept(trueOutputExec);

            var falseOutputExec = ExecPin.outputPin();
            onPinCreated.accept(falseOutputExec);

            var conditionInput = VarPin.inputPin(RED);
            onPinCreated.accept(conditionInput);

            var node = NodeBuilder.builder(HEADER_COLORS.UTIL,"Branch")
                    .addPin(inputExec, "")
                    .addPin(conditionInput, "Condition")
                    .addPin(trueOutputExec, "True")
                    .addPin(falseOutputExec, "False")
                    .build();

            onNodeCreated.accept(node);

            return node;
        }
    },
    ON_START_EVENT("Startup Event"){
        @Override
        public View createNode(int nodeColor, Consumer<Pin> onPinCreated, Consumer<BPNode> onNodeCreated) {
//            var inputExec = ExecPin.inputPin();
//            onPinCreated.accept(inputExec);

            var outputExec = ExecPin.outputPin();
            onPinCreated.accept(outputExec);

            var output = VarPin.outputPin(BLUE);
            onPinCreated.accept(output);

            var node = NodeBuilder.builder(HEADER_COLORS.EVENT,"Event onStartup")
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

            var node = NodeBuilder.builder(HEADER_COLORS.FUNCTION, "Print string")
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
