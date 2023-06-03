package com.rodev.jbpcore.blueprint.pin.list_pin;

import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.behaviour.InputBehaviour;
import com.rodev.jbpcore.blueprint.pin.behaviour.OutputBehaviour;
import com.rodev.jbpcore.blueprint.pin.list_pin.dynamic.DynamicListPin;
import org.jetbrains.annotations.Nullable;

public class InputListBehaviour extends InputBehaviour {

    @Override
    public boolean isApplicable(Pin target, Pin another) {
        var anotherList = ListPin.castToListPinOrNull(another);

        if(anotherList == null) return false;

        var anotherBehaviour = another.getConnectionBehaviour();

        // Todo handle in derived class
        if(anotherList instanceof DynamicListPin dynamicListPin) {
            if(!dynamicListPin.isDynamicVariableSet()) {
                return anotherBehaviour.isOutput();
            }
        }

        if(anotherBehaviour instanceof InputListBehaviour) return false;

        var targetList = (ListPin) target;
        var targetElementType = targetList.getElementType();
        var anotherElementType = anotherList.getElementType();

        return targetElementType.equals(anotherElementType) && anotherBehaviour.isOutput();
    }

}
