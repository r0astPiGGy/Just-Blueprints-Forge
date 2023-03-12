package com.rodev.jbpcore.blueprint.data.action;

import com.rodev.jbpcore.blueprint.data.json.ActionEntity;
import com.rodev.jbpcore.blueprint.data.variable.VariableType;

public interface PinTypeFactory {

    PinType create(ActionEntity.PinTypeEntity entity, VariableType variableType);

}
