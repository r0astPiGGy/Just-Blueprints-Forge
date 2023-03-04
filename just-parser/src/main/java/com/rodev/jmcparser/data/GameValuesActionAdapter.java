package com.rodev.jmcparser.data;

import com.rodev.jmcparser.json.GameValue;
import com.rodev.test.blueprint.data.action.Action;
import com.rodev.test.blueprint.data.json.ActionEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GameValuesActionAdapter {

    private final GameValue[] gameValues;
    private final Set<String> gameValueIdsWithDisabledSelectors = new HashSet<>();

    {
        gameValueIdsWithDisabledSelectors.addAll(List.of(
                "cpu_usage",
                "server_tps",
                "timestamp",
                "server_current_tick",
                "selection_size",
                "selection_target_names",
                "selection_target_uuids",
                "url_response",
                "url_response_code",
                "url",
                "world_time",
                "world_weather",
                "server_stopped_time",
                "action_count_per_tick",
                "owner_uuid",
                "world_size",
                "world_id"
        ));
    }

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
        action.icon_namespace = "game_values";
        action.extra_data = getExtraDataFor(gameValue);

        var outputPin = new ActionEntity.PinTypeEntity();
        outputPin.type = gameValue.type;
        outputPin.id = gameValue.id;
        outputPin.label = "";

        action.output = List.of(outputPin);

        return action;
    }

    @Nullable
    private Object getExtraDataFor(GameValue gameValue) {
        if(gameValueIdsWithDisabledSelectors.contains(gameValue.id) || gameValue.id.startsWith("event_")) {
            return new HashMap<>(){{
                put("selector_disabled", true);
            }};
        }

        return null;
    }

}
