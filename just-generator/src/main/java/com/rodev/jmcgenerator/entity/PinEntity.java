package com.rodev.jmcgenerator.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class PinEntity {
    @JsonAlias("name")
    public String id;

    @Nullable
    public String connectedTo;

    @Nullable
    public String value;

    public String type;

    @JsonIgnore
    public final GeneratorRelatedData data = new GeneratorRelatedData();

    @JsonIgnore
    public boolean isExecType() {
        return type.equals("exec");
    }

    @JsonIgnore
    public boolean isConnected() {
        return connectedTo != null;
    }

    public static class GeneratorRelatedData {
        public NodeEntity parent;

        private Boolean input;

        // Если Pin является аргументом, то это поле соединено с Pin возвращаемого типа
        private PinEntity inputPinConnectedTo;
        // Если Pin является возвращаемым типом, то в этом списке Pin'ы типа аргумент
        private final List<PinEntity> outputPinConnectedTo = new LinkedList<>();

        public void setTypeOfInput() {
            pinTypeMustBeNull();

            input = true;
        }

        public void setTypeOfOutput() {
            pinTypeMustBeNull();
            input = false;
        }

        // Только если является аргументом
        @Nullable
        public PinEntity getConnection() {
            mustBeTypeofInput();

            return inputPinConnectedTo;
        }

        public void setConnection(@NotNull PinEntity output) {
            mustBeTypeofInput();
            inputPinConnectedTo = output;
        }

        @NotNull
        public List<PinEntity> getConnections() {
            mustBeTypeofOutput();

            return outputPinConnectedTo;
        }

        public void addConnection(@NotNull PinEntity input) {
            mustBeTypeofOutput();

            outputPinConnectedTo.add(input);
        }

        private void pinTypeNotNull() {
            if(input == null) throw new IllegalStateException("Pin type is not specified.");
        }

        private void pinTypeMustBeNull() {
            if(input != null) throw new IllegalStateException("Cannot change pin type");
        }

        private void mustBeTypeofInput() {
            pinTypeNotNull();

            if(!input) throw new IllegalStateException("This pin is type of Output, use #getConnections()");
        }

        private void mustBeTypeofOutput() {
            pinTypeNotNull();

            if(input) throw new IllegalStateException("This pin is type of Input, use #getConnection()");
        }
    }
}
