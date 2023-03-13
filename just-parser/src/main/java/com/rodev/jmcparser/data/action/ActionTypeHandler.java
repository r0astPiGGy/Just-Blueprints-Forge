package com.rodev.jmcparser.data.action;

import com.rodev.jmcparser.json.ActionData;

public interface ActionTypeHandler {

    String handleActionType(ActionData data);

}
