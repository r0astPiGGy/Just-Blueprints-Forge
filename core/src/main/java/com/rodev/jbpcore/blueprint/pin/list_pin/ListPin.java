package com.rodev.jbpcore.blueprint.pin.list_pin;

import com.rodev.jbpcore.blueprint.pin.PinImpl;
import com.rodev.jbpcore.blueprint.pin.behaviour.ConnectionBehaviour;
import com.rodev.jbpcore.data.action.pin_type.PinType;
import com.rodev.jbpcore.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ListPin extends PinImpl {

    private final VariableType elementType;

    protected ListPin(ListPinType pinType, ConnectionBehaviour connectionBehaviour) {
        super(pinType, connectionBehaviour);

        elementType = pinType.getElementType();
    }

    public static ListPin inputPin(ListPinType pinType) {
        return new ListPin(pinType, new InputListBehaviour());
    }

    public static ListPin outputPin(ListPinType pinType) {
        return new ListPin(pinType, new OutputListBehaviour());
    }

    @NotNull
    public VariableType getElementType() {
        return elementType;
    }

    @Override
    public int getColor() {
        return getElementType().color();
    }

    @Nullable
    public static ListPin castToListPinOrNull(Pin pin) {
        return pin instanceof ListPin ? (ListPin) pin : null;
    }
}
