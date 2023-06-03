package com.rodev.jbpcore.blueprint.pin.list_pin.dynamic;

import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.list_pin.InputListBehaviour;
import com.rodev.jbpcore.blueprint.pin.list_pin.ListPin;

public class DynamicInputListBehaviour extends InputListBehaviour {

    @Override
    public boolean isApplicable(Pin target, Pin another) {
        var targetList = DynamicListPin.castToDynamicList(target);

        if(targetList.isDynamicVariableSet()) {
            return super.isApplicable(target, another);
        }

        if(another.getConnectionBehaviour().isInput()) return false;

        return another instanceof ListPin;
    }
}
