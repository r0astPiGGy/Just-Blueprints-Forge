package com.rodev.jbpcore.blueprint.pin.exec_pin;

import com.rodev.jbpcore.blueprint.pin.PinImpl;
import com.rodev.jbpcore.blueprint.pin.behaviour.ConnectionBehaviour;
import com.rodev.jbpcore.blueprint.pin.behaviour.InputBehaviour;
import com.rodev.jbpcore.blueprint.pin.behaviour.OutputBehaviour;
import com.rodev.jbpcore.data.action.pin_type.PinType;
import com.rodev.jbpcore.blueprint.pin.Pin;

public class ExecPin extends PinImpl {

    protected ExecPin(PinType pinType, ConnectionBehaviour connectionBehaviour) {
        super(pinType, connectionBehaviour);
    }

    public static Pin outputPin(PinType pinType) {
        return new ExecPin(pinType, new ExecOutputBehaviour());
    }

    public static Pin inputPin(PinType pinType) {
        return new ExecPin(pinType, new ExecInputBehaviour());
    }

    private static boolean isNotExecPin(Pin pin) {
        return !(pin instanceof ExecPin);
    }

    private static class ExecInputBehaviour extends InputBehaviour {

        @Override
        public boolean isApplicable(Pin target, Pin another) {
            if(isNotExecPin(another)) return false;

            var connectionBehaviour = another.getConnectionBehaviour();

            if(connectionBehaviour instanceof ExecInputBehaviour) return false;

            return connectionBehaviour.isOutput();
        }
    }

    private static class ExecOutputBehaviour extends OutputBehaviour {

        @Override
        public boolean isApplicable(Pin target, Pin another) {
            if(isNotExecPin(another)) return false;

            var connectionBehaviour = another.getConnectionBehaviour();

            if(connectionBehaviour instanceof ExecOutputBehaviour) return false;

            return connectionBehaviour.isInput();
        }

        @Override
        public boolean supportMultipleConnections() {
            return false;
        }
    }

}
