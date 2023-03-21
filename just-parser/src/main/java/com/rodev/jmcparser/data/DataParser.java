package com.rodev.jmcparser.data;

import com.rodev.jmcparser.data.action.ActionParser;
import com.rodev.jmcparser.data.category.CategoryProvider;
import com.rodev.jmcparser.data.event.EventOutputFiller;
import com.rodev.jmcparser.data.event.EventParser;
import com.rodev.jmcparser.data.game_value.GameValueParser;
import com.rodev.jmcparser.data.game_value.GameValueTranslator;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

public class DataParser {
    @Setter
    private Runnable onAllDataLoaded = () -> {};

    private final List<Parser<?, ?>> parsers = new LinkedList<>();
    private final List<Interpreter<?>> interpreters = new LinkedList<>();

    public void registerParser(Parser<?, ?> parser) {
        parsers.add(parser);
    }

    public void registerInterpreter(Interpreter<?> interpreter) {
        interpreters.add(interpreter);
    }

    public void parseUsing(DataProvider dataProvider) {
        for(var parser : parsers) {
            parser.load(dataProvider);
        }

        onAllDataLoaded.run();
    }

    public DataInterpreter createInterpreter() {
        var interpreter = new DataInterpreter();

        for(var parser : parsers) {
            interpreter.registerInterpreter(parser.createInterpreter());
        }

        for(var registered : interpreters) {
            interpreter.registerInterpreter(registered);
        }

        return interpreter;
    }

}
