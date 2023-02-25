package com.rodev.test.blueprint.data.action;

import com.rodev.test.blueprint.data.json.ActionEntity;
import com.rodev.test.blueprint.data.variable.VariableType;

public interface PinTypeFactory {

    PinType create(ActionEntity.PinTypeEntity entity, VariableType variableType);

}
