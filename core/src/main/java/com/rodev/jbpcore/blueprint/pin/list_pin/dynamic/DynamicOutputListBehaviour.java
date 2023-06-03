package com.rodev.jbpcore.blueprint.pin.list_pin.dynamic;

import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.list_pin.ListPin;
import com.rodev.jbpcore.blueprint.pin.list_pin.OutputListBehaviour;

public class DynamicOutputListBehaviour extends OutputListBehaviour {

    @Override
    public boolean isApplicable(Pin target, Pin another) {
        var targetList = DynamicListPin.castToDynamicList(target);

        if(targetList.isDynamicVariableSet()) {
            return super.isApplicable(target, another);
        }

        if(another.getConnectionBehaviour().isOutput()) return false;

        return another instanceof ListPin;
    }
}
