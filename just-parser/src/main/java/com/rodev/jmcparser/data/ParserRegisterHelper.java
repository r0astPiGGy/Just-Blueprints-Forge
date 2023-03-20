package com.rodev.jmcparser.data;

import com.rodev.jbpcore.utils.StringUtils;
import com.rodev.jmcparser.data.action.ActionInterpreter;
import com.rodev.jmcparser.data.action.ActionParser;
import com.rodev.jmcparser.data.action.custom.CustomActionParser;
import com.rodev.jmcparser.data.category.CategoryProvider;
import com.rodev.jmcparser.data.event.EventOutputFiller;
import com.rodev.jmcparser.data.event.EventParser;
import com.rodev.jmcparser.data.game_value.GameValueMappings;
import com.rodev.jmcparser.data.game_value.GameValueParser;
import com.rodev.jmcparser.data.game_value.GameValueTranslator;
import com.rodev.jmcparser.generator.DataGenerator;
import com.rodev.jmcparser.json.Event;
import com.rodev.jmcparser.json.GameValue;
import lombok.AccessLevel;
import lombok.Setter;

public class ParserRegisterHelper {

    private final LocaleProvider localeProvider;
    private final CategoryProvider categoryProvider;
    private final DataParser dataParser;

    private final GameValueMappings gameValueMappings = new GameValueMappings();

    private final DataGenerator dataGenerator;

    @Setter(AccessLevel.PRIVATE)
    private Event[] events;

    @Setter(AccessLevel.PRIVATE)
    private GameValue[] gameValues;

    public ParserRegisterHelper(LocaleProvider localeProvider, CategoryProvider categoryProvider, DataParser dataParser) {
        this.localeProvider = localeProvider;
        this.categoryProvider = categoryProvider;
        this.dataParser = dataParser;

        dataGenerator = new DataGenerator(gameValueMappings);
    }

    public void registerParsers() {
        dataParser.setOnAllDataLoaded(this::onAllDataLoaded);

        registerActionParser();
        registerCustomActionParser();
        registerEventParser();
        registerGameValueParser();
    }

    public void onDataProvide(DataProvider dataProvider) {
        gameValueMappings.load(dataProvider);
    }

    private void registerCustomActionParser() {
        var parser = new CustomActionParser();
        parser.setOnInterpreterCreatedListener(i -> {
            i.addOnActionInterpretedListener((e, e1) -> dataGenerator.onCustomActionInterpreted(e1));
        });
        dataParser.registerParser(parser);
    }

    private void registerActionParser() {
        var parser = new ActionParser(localeProvider, categoryProvider);
        parser.setOnInterpreterCreatedListener(this::onActionInterpreterCreated);
        dataParser.registerParser(parser);
    }

    private void onActionInterpreterCreated(ActionInterpreter interpreter) {
        interpreter.setActionTypeHandler(data -> {
            var containing = data.containing;

            if (containing != null && containing.equalsIgnoreCase("predicate")) {
                return "pure_function";
            }

            if (data.id.startsWith("set_variable_get")) {
                if (data.args[1].type.equals(data.origin) && data.args.length < 3)
                    return "variable_property";

                return "pure_function";
            }

            return "function";
        });
        interpreter.addOnActionInterpretedListener(dataGenerator::onActionInterpreted);
        interpreter.setPinTypeNameHandler(s -> {
            if(s.equalsIgnoreCase("variable")) return "Переменная";

            return StringUtils.capitalize(s);
        });
    }

    private void registerEventParser() {
        var parser = new EventParser(localeProvider);
        parser.setOnDataChangedListener(this::setEvents);
        parser.setOnInterpreterCreatedListener(i -> {
            i.addOnActionInterpretedListener(dataGenerator::onEventInterpreted);
        });
        dataParser.registerParser(parser);
    }

    private void registerGameValueParser() {
        var parser = new GameValueParser();
        parser.setOnDataChangedListener(this::setGameValues);
        parser.setOnInterpreterCreatedListener(i -> {
            i.addOnActionInterpretedListener(dataGenerator::onGameValueInterpreted);
        });
        dataParser.registerParser(parser);
    }

    public void onAllDataInterpreted() {

    }

    public DataGenerator getDataGenerator() {
        return dataGenerator;
    }

    private void onAllDataLoaded() {
        GameValueTranslator.translate(gameValues, localeProvider);
        EventOutputFiller.fill(events, gameValues);
    }

}
