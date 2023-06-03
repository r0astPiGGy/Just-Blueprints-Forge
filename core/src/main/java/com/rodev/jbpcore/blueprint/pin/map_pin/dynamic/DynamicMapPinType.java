package com.rodev.jbpcore.blueprint.pin.map_pin.dynamic;

import com.rodev.jbpcore.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.dynamic.DynamicDependency;
import com.rodev.jbpcore.blueprint.pin.dynamic.DynamicPinDestination;
import com.rodev.jbpcore.blueprint.pin.map_pin.MapPinType;
import org.jetbrains.annotations.Nullable;

public class DynamicMapPinType extends MapPinType {

    private final DynamicPinDestination keyDestination;
    private final DynamicPinDestination valueDestination;
    private final String keyDependsOn;
    private final String valueDependsOn;

    public DynamicMapPinType(
            String id,
            String name,
            VariableType type,
            VariableType keyType,
            VariableType valueType,
            @Nullable DynamicDependency dynamicKeyDependency,
            @Nullable DynamicDependency dynamicValueDependency
    ) {
        super(id, name, type, keyType, valueType);

        if(dynamicValueDependency == null) {
            valueDestination = null;
            valueDependsOn = null;
        } else {
            valueDestination = dynamicValueDependency.getDestination();
            valueDependsOn = dynamicValueDependency.dependsOn();
        }

        if(dynamicKeyDependency == null) {
            keyDestination = null;
            keyDependsOn = null;
        } else {
            keyDestination = dynamicKeyDependency.getDestination();
            keyDependsOn = dynamicKeyDependency.dependsOn();
        }
    }

    @Nullable
    public DynamicPinDestination getKeyDestination() {
        return keyDestination;
    }

    @Nullable
    public DynamicPinDestination getValueDestination() {
        return valueDestination;
    }

    @Nullable
    public String getKeyDependsOn() {
        return keyDependsOn;
    }

    @Nullable
    public String getValueDependsOn() {
        return valueDependsOn;
    }

    @Override
    public Pin createInputPin() {
        return DynamicMapPin.inputPin(this);
    }

    @Override
    public Pin createOutputPin() {
        return DynamicMapPin.outputPin(this);
    }
}
