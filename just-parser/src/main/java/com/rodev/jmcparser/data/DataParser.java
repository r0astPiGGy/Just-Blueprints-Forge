package com.rodev.jmcparser.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodev.jmcparser.json.ActionData;
import com.rodev.jmcparser.json.DataBlob;
import com.rodev.jmcparser.json.Event;
import com.rodev.jmcparser.json.GameValue;
import com.rodev.jmcparser.util.TimeCounter;
import com.rodev.jbpcore.blueprint.data.json.ActionEntity;

import java.io.IOException;
import java.io.InputStream;

public class DataParser {

    private DataBlob dataBlob;
    private ActionData[] actionData;
    private GameValue[] gameValues;
    private ActionEntity[] customActions;
    private Event[] events;

    public void parseUsing(DataProvider dataProvider) {
        var counter = new TimeCounter();

        dataProvider.loadActionsAndApply(this::parseActions);
        counter.printAndReset(ms -> "Loaded " + actionData.length + " actions in " + ms + "ms.");

        dataProvider.loadEventsAndApply(this::parseEvents);
        counter.printAndReset(ms -> "Loaded " + events.length + " events in " + ms + "ms.");

        dataProvider.loadGameValuesAndApply(this::parseGameValues);
        counter.printAndReset(ms -> "Loaded " + gameValues.length + " game values in " + ms + "ms.");

        dataProvider.loadCustomActionsAndApply(this::parseCustomActions);

        dataBlob = new DataBlob(actionData, events, gameValues, customActions);
    }

    private void parseActions(InputStream inputStream) {
        actionData = parse(inputStream, ActionData[].class);
    }

    private void parseEvents(InputStream inputStream) {
        events = parse(inputStream, Event[].class);
    }

    private void parseGameValues(InputStream inputStream) {
        gameValues = parse(inputStream, GameValue[].class);
    }

    private void parseCustomActions(InputStream inputStream) {
        customActions = parse(inputStream, ActionEntity[].class);
    }

    private <T> T parse(InputStream is, Class<T> clazz) {
        try {
            return new ObjectMapper().readValue(is, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public DataInterpreter createInterpreter(LocaleProvider localeProvider, CategoryProvider categoryProvider) {
        return new DataInterpreter(dataBlob, localeProvider, categoryProvider);
    }

}
