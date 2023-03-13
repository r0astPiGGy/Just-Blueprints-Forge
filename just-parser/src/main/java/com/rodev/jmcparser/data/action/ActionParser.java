package com.rodev.jmcparser.data.action;

import com.rodev.jmcparser.data.DataProvider;
import com.rodev.jmcparser.data.Interpreter;
import com.rodev.jmcparser.data.LocaleProvider;
import com.rodev.jmcparser.data.Parser;
import com.rodev.jmcparser.data.category.CategoryProvider;
import com.rodev.jmcparser.json.ActionData;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class ActionParser extends Parser<ActionData, ActionInterpreter> {

    private final LocaleProvider localeProvider;
    private final CategoryProvider categoryProvider;

    @Override
    public void load(DataProvider dataProvider) {
        dataProvider.loadActionsAndApply(is -> {
            setParsedData(Parser.parseJson(is, ActionData[].class));
        });
    }

    @Override
    protected ActionInterpreter createInterpreter(ActionData[] parsedData) {
        return new ActionInterpreter(parsedData, localeProvider, categoryProvider);
    }
}
