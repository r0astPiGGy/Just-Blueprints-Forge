package com.rodev.jmcparser.data.event;

import com.rodev.jmcparser.data.DataProvider;
import com.rodev.jmcparser.data.LocaleProvider;
import com.rodev.jmcparser.data.Parser;
import com.rodev.jmcparser.json.Event;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EventParser extends Parser<Event, EventInterpreter> {

    private final LocaleProvider localeProvider;

    @Override
    public void load(DataProvider dataProvider) {
        dataProvider.loadEventsAndApply(is -> {
            setParsedData(Parser.parseJson(is, Event[].class));
        });
    }

    @Override
    protected EventInterpreter createInterpreter(Event[] parsedData) {
        return new EventInterpreter(parsedData, localeProvider);
    }
}
