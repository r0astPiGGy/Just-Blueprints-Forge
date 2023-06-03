package com.rodev.jbpcore.blueprint.pin.map_pin.dynamic;

import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.map_pin.InputMapBehaviour;
import com.rodev.jbpcore.blueprint.pin.map_pin.MapPin;

public class DynamicInputMapBehaviour extends InputMapBehaviour {

    @Override
    public boolean isApplicable(Pin target, Pin another) {
        var mapTarget = (DynamicMapPin) target;

        if(mapTarget.isDynamicVariableSet()) {
            return super.isApplicable(target, another);
        }

        if(another.getConnectionBehaviour().isInput()) return false;

        return another instanceof MapPin;
    }
}
