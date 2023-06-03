package com.rodev.jbpcore.blueprint.pin.map_pin;

import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.behaviour.InputBehaviour;
import com.rodev.jbpcore.blueprint.pin.map_pin.dynamic.DynamicMapPin;

public class InputMapBehaviour extends InputBehaviour {

    @Override
    public boolean isApplicable(Pin target, Pin pin) {
        var anotherMap = MapPin.castToMapOrNull(pin);

        if(anotherMap == null) return false;

        var anotherBehaviour = anotherMap.getConnectionBehaviour();
        var targetMap = (MapPin) target;

        if(anotherMap instanceof DynamicMapPin dynamicMapPin) {
            if(!anotherBehaviour.isOutput()) return false;

            if(dynamicMapPin.isDynamicKeySet()) {
                if(!dynamicMapPin.getKeyType().equals(targetMap.getKeyType())) {
                    return false;
                }
            }
            if(dynamicMapPin.isDynamicValueSet()) {
                return dynamicMapPin.getValueType().equals(targetMap.getValueType());
            }
            return true;
        }

        if(anotherBehaviour instanceof InputMapBehaviour) return false;

        var anotherKeyType = anotherMap.getKeyType();
        var anotherValueType = anotherMap.getValueType();

        return targetMap.getKeyType().equals(anotherKeyType) &&
                targetMap.getValueType().equals(anotherValueType) &&
                anotherBehaviour.isOutput();
    }
}
