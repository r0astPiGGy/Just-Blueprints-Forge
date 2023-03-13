package com.rodev.jmcparser.data.event;

import com.rodev.jmcparser.data.LocaleProvider;
import com.rodev.jmcparser.json.ActionData;
import com.rodev.jmcparser.json.Event;

public interface EventNameHandler {

    String handleEventName(Event data, LocaleProvider localeProvider);

}
