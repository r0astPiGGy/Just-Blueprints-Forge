package com.rodev.jmcparser.data;

import com.rodev.jmcparser.json.GameValue;
import com.rodev.test.blueprint.data.action.Action;
import com.rodev.test.blueprint.data.json.ActionEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GameValuesActionAdapter {

    private final GameValue[] gameValues;

    public static ActionEntity[] adapt(GameValue[] gameValues) {
        return new GameValuesActionAdapter(gameValues).adapt();
    }

    public ActionEntity[] adapt() {
        var actions = new ActionEntity[gameValues.length];

        for(int i = 0; i < gameValues.length; i++) {
            actions[i] = adapt(gameValues[i]);
        }

        return actions;
    }

    private ActionEntity adapt(GameValue gameValue) {
        var action = new ActionEntity();

        action.name = gameValue.name;
        action.id = gameValue.id + "_gamevalue_getter";
        action.category = "game_values";
        action.type = "game_value_getter";
        action.input = Collections.emptyList();

        var outputPin = new ActionEntity.PinTypeEntity();
        outputPin.type = gameValue.type;
        outputPin.id = gameValue.id;
        outputPin.label = "";

        action.output = List.of(outputPin);

        return action;
    }

}
