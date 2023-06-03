package com.rodev.jbpcore.data.action.pin_type;

import com.rodev.jbpcore.data.json.ActionEntity;
import com.rodev.jbpcore.data.variable.VariableType;

public interface PinTypeFactory {

    PinType create(ActionEntity.PinTypeEntity entity, VariableType variableType);

}
