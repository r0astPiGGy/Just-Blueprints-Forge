package com.rodev.jmcparser.data.action.custom;

import com.rodev.jmcparser.data.DataProvider;
import com.rodev.jmcparser.data.Parser;

public class CustomActionParser extends Parser<CustomActionEntity, CustomActionInterpreter> {
    @Override
    public void load(DataProvider dataProvider) {
        dataProvider.loadCustomActionsAndApply(is -> {
            setParsedData(Parser.parseJson(is, CustomActionEntity[].class));
        });
    }

    @Override
    protected CustomActionInterpreter createInterpreter(CustomActionEntity[] parsedData) {
        return new CustomActionInterpreter(parsedData);
    }
}
