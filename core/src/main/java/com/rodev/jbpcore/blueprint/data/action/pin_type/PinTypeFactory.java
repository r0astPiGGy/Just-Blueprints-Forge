package com.rodev.jbpcore.blueprint.data.action.pin_type;

import com.rodev.jbpcore.blueprint.data.json.ActionEntity;
import com.rodev.jbpcore.blueprint.data.variable.VariableType;

public interface PinTypeFactory {

    PinType create(ActionEntity.PinTypeEntity entity, VariableType variableType);

}
