package com.rodev.jmcparser.data;

import com.rodev.jmcparser.json.ActionData;

public interface ActionNameHandler {

    String handleActionName(ActionData data, LocaleProvider localeProvider);

}
