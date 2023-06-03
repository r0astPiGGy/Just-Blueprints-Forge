package com.rodev.jbpcore.blueprint.pin.map_pin.dynamic;

import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.map_pin.MapPin;
import com.rodev.jbpcore.blueprint.pin.map_pin.OutputMapBehaviour;

public class DynamicOutputMapBehaviour extends OutputMapBehaviour {

    @Override
    public boolean isApplicable(Pin target, Pin another) {
        var mapTarget = (DynamicMapPin) target;

        if(mapTarget.isDynamicVariableSet()) {
            return super.isApplicable(target, another);
        }

        if(another.getConnectionBehaviour().isOutput()) return false;

        return another instanceof MapPin;
    }
}
