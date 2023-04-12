package com.rodev.jbpcore.blueprint.pin.dynamic;

import com.rodev.jbpcore.blueprint.pin.PinConnectionListener;
import com.rodev.jbpcore.blueprint.pin.dynamic.impl.DynamicGroupImpl;

public interface DynamicGroup extends PinConnectionListener {

    void addDependency(Dynamic pin, DynamicBehaviour dynamicBehaviour);

    static DynamicGroup of(Dynamic root, DynamicBehaviour dynamicBehaviour, DynamicBehaviour... behaviours) {
        return new DynamicGroupImpl(root, dynamicBehaviour, behaviours);
    }

}
