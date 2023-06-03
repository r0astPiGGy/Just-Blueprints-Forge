package com.rodev.jmcparser.data.game_value;

import com.rodev.jbpcore.data.json.ActionEntity;
import com.rodev.jmcparser.data.Interpreter;
import com.rodev.jmcparser.json.GameValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GameValueInterpreter extends Interpreter<GameValue> {

    private final Set<String> gameValueIdsWithDisabledSelectors = new HashSet<>();

    public GameValueInterpreter(GameValue[] data) {
        super(data);
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

    protected ActionEntity interpret(@NotNull GameValue gameValue) {
        var action = new ActionEntity();

        action.name = gameValue.name;
        action.id = gameValue.id + "_gamevalue_getter";
        action.category = "game_values";
        action.type = "game_value_getter";
        action.input = Collections.emptyList();
        action.output = createOutput(gameValue);
        action.extra_data = getExtraDataFor(gameValue);
        action.icon_namespace = "game_values";

        return action;
    }

    private List<ActionEntity.PinTypeEntity> createOutput(GameValue gameValue) {
        var outputPin = new ActionEntity.PinTypeEntity();
        outputPin.type = gameValue.type;
        outputPin.id = gameValue.id;
        outputPin.label = "";

        applyExtraDataToGameValue(outputPin, gameValue);

        return List.of(outputPin);
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
