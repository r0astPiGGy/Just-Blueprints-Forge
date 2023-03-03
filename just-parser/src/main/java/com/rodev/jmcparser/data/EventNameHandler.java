package com.rodev.jmcparser.data;

import com.rodev.jmcparser.json.ActionData;
import com.rodev.jmcparser.json.Event;

public interface EventNameHandler {

    String handleEventName(Event data, LocaleProvider localeProvider);

}
