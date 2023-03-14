package com.rodev.jmcparser.data;

import com.rodev.jbpcore.blueprint.data.json.ActionEntity;
import com.rodev.jbpcore.utils.StringUtils;
import com.rodev.jmcparser.data.action.ActionInterpreter;
import com.rodev.jmcparser.data.action.ActionParser;
import com.rodev.jmcparser.data.action.custom.CustomActionParser;
import com.rodev.jmcparser.data.category.CategoryProvider;
import com.rodev.jmcparser.data.event.EventOutputFiller;
import com.rodev.jmcparser.data.event.EventParser;
import com.rodev.jmcparser.data.game_value.GameValueParser;
import com.rodev.jmcparser.data.game_value.GameValueTranslator;
import com.rodev.jmcparser.json.ActionData;
import com.rodev.jmcparser.json.Event;
import com.rodev.jmcparser.json.GameValue;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class ParserRegisterHelper {

    private final LocaleProvider localeProvider;
    private final CategoryProvider categoryProvider;
    private final DataParser dataParser;

    @Setter(AccessLevel.PRIVATE)
    private Event[] events;

    @Setter(AccessLevel.PRIVATE)
    private GameValue[] gameValues;

    public void registerParsers() {
        dataParser.setOnAllDataLoaded(this::onAllDataLoaded);

        registerActionParser();
        registerCustomActionParser();
        registerEventParser();
        registerGameValueParser();
    }

    private void registerCustomActionParser() {
        var parser = new CustomActionParser();
        parser.setOnInterpreterCreatedListener(i -> {
            i.addOnActionInterpretedListener((e, e1) -> onCustomActionInterpreted(e));
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

            if (data.id.startsWith("set_variable_get") && data.args.length < 3) {
                if (data.args[1].type.equals(data.origin))
                    return "variable_property";
            }

            return "function";
        });
        interpreter.addOnActionInterpretedListener(this::onActionInterpreted);
        interpreter.setPinTypeNameHandler(StringUtils::capitalize);
    }

    private void registerEventParser() {
        var parser = new EventParser(localeProvider);
        parser.setOnDataChangedListener(this::setEvents);
        parser.setOnInterpreterCreatedListener(i -> {
            i.addOnActionInterpretedListener(this::onEventInterpreted);
        });
        dataParser.registerParser(parser);
    }

    private void registerGameValueParser() {
        var parser = new GameValueParser();
        parser.setOnDataChangedListener(this::setGameValues);
        parser.setOnInterpreterCreatedListener(i -> {
            i.addOnActionInterpretedListener(this::onGameValueInterpreted);
        });
        dataParser.registerParser(parser);
    }

    private void onActionInterpreted(ActionEntity action, ActionData data) {

    }

    private void onCustomActionInterpreted(ActionEntity action) {

    }

    private void onEventInterpreted(ActionEntity action, Event data) {

    }

    private void onGameValueInterpreted(ActionEntity action, GameValue data) {

    }

    private void onAllDataLoaded() {
        GameValueTranslator.translate(gameValues, localeProvider);
        EventOutputFiller.fill(events, gameValues);
    }

}
