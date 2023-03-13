package com.rodev.jmcparser.data.game_value;

import com.rodev.jmcparser.data.DataProvider;
import com.rodev.jmcparser.data.Interpreter;
import com.rodev.jmcparser.data.Parser;
import com.rodev.jmcparser.json.GameValue;

public class GameValueParser extends Parser<GameValue, GameValueInterpreter> {

    @Override
    public void load(DataProvider dataProvider) {
        dataProvider.loadGameValuesAndApply(is -> {
            setParsedData(Parser.parseJson(is, GameValue[].class));
        });
    }

    @Override
    protected GameValueInterpreter createInterpreter(GameValue[] parsedData) {
        return new GameValueInterpreter(parsedData);
    }
}
