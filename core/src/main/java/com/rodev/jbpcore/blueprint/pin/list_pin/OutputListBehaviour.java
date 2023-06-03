package com.rodev.jbpcore.blueprint.pin.list_pin;

import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.behaviour.OutputBehaviour;
import com.rodev.jbpcore.blueprint.pin.list_pin.dynamic.DynamicListPin;

public class OutputListBehaviour extends OutputBehaviour {

    @Override
    public boolean isApplicable(Pin target, Pin another) {
        var anotherList = ListPin.castToListPinOrNull(another);

        if(anotherList == null) return false;

        var anotherBehaviour = another.getConnectionBehaviour();

        if(anotherList instanceof DynamicListPin dynamicListPin) {
            if(!dynamicListPin.isDynamicVariableSet()) {
                return anotherBehaviour.isInput();
            }
        }

        if(anotherBehaviour instanceof OutputListBehaviour) return false;

        var targetList = (ListPin) target;
        var targetElementType = targetList.getElementType();
        var anotherElementType = anotherList.getElementType();

        return targetElementType.equals(anotherElementType) && anotherBehaviour.isInput();
    }

}
