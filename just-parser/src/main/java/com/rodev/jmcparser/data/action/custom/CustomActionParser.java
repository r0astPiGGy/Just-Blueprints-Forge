package com.rodev.jmcparser.data.action.custom;

import com.rodev.jbpcore.blueprint.data.json.ActionEntity;
import com.rodev.jmcparser.data.DataProvider;
import com.rodev.jmcparser.data.Parser;

public class CustomActionParser extends Parser<ActionEntity, CustomActionInterpreter> {
    @Override
    public void load(DataProvider dataProvider) {
        dataProvider.loadCustomActionsAndApply(is -> {
            setParsedData(Parser.parseJson(is, ActionEntity[].class));
        });
    }

    @Override
    protected CustomActionInterpreter createInterpreter(ActionEntity[] parsedData) {
        return new CustomActionInterpreter(parsedData);
    }
}
