package com.rodev.jmcparser.data.action;

import com.rodev.jmcparser.data.LocaleProvider;
import com.rodev.jmcparser.json.ActionData;

public interface ActionNameHandler {

    String handleActionName(ActionData data, LocaleProvider localeProvider);

}
