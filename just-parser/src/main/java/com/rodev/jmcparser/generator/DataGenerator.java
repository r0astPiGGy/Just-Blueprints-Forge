package com.rodev.jmcparser.generator;

import com.rodev.jbpcore.blueprint.data.json.ActionEntity;
import com.rodev.jmcgenerator.entity.GeneratorEntity;
import com.rodev.jmcparser.data.action.custom.CustomActionEntity;
import com.rodev.jmcparser.data.game_value.GameValueMappings;
import com.rodev.jmcparser.json.ActionData;
import com.rodev.jmcparser.json.Event;
import com.rodev.jmcparser.json.GameValue;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@RequiredArgsConstructor
public class DataGenerator {

    private final List<GeneratorEntity> generatorEntities = new LinkedList<>();

    private final GameValueMappings gameValueMappings;

    public GeneratorEntity[] getGeneratorEntities() {
        return generatorEntities.toArray(new GeneratorEntity[0]);
    }

    public void onActionInterpreted(ActionEntity action, ActionData data) {
        var generatorEntity = new GeneratorEntity();
        generatorEntity.id = data.id;

        StringBuilder builder = new StringBuilder();

        builder.append(data.object);
        builder.append("::");
        builder.append(data.name);
        if(isContainingSelector(data)) {
            var selectorId = action.input.get(0).id;
            builder.append("<$");
            builder.append(selectorId);
            builder.append(">");
            generatorEntity.ignoreArguments = Set.of(selectorId);
        }
        builder.append("($args)");
        if(data.containing == null || !data.containing.equals("predicate")) {
            builder.append(";");
        }

        generatorEntity.schema = builder.toString();

        generatorEntities.add(generatorEntity);
    }

    private boolean isContainingSelector(ActionData actionData) {
        return switch (actionData.object) {
            case "entity", "player" -> true;
            default -> false;
        };
    }

    public void onCustomActionInterpreted(CustomActionEntity action) {
        if(action.generator == null)
            throw new IllegalStateException("Custom action by id " + action.id + " doesn't have generator entity.");

        var generator = action.generator;

        generator.id = action.id;

        generatorEntities.add(generator);
    }

    public void onEventInterpreted(ActionEntity action, Event data) {
        var generatorEntity = new GeneratorEntity();
        generatorEntity.id = data.id;
        generatorEntity.type = "root";

        List<String> schema = new LinkedList<>();

        String builder = "event<" + data.id + "> {";

        schema.add(builder);
        schema.add("$exec");
        schema.add("}");

        generatorEntity.schema = schema;

        var output = new HashMap<String, String>();
        for(var eventOutput : data.rawOutput) {
            output.put(eventOutput.id, getSchemaForGameValue(eventOutput));
        }

        generatorEntity.output = output;

        generatorEntities.add(generatorEntity);
    }

    public void onGameValueInterpreted(ActionEntity action, GameValue data) {
        var generatorEntity = new GeneratorEntity();
        generatorEntity.id = action.id;

        StringBuilder builder = new StringBuilder();

        String realId = gameValueMappings.getRealId(data.id);

        builder.append("value::");
        builder.append(realId);
        if(isSelectorEnabled(action)) {
            builder.append("<$");
            builder.append(data.id);
            builder.append(">");
        }

        generatorEntity.schema = builder.toString();

        generatorEntities.add(generatorEntity);
    }

    private String getSchemaForGameValue(GameValue gameValue) {
        StringBuilder builder = new StringBuilder();

        String realId = gameValueMappings.getRealId(gameValue.id);

        builder.append("value::");
        builder.append(realId);

        return builder.toString();
    }

    private boolean isSelectorEnabled(@NotNull ActionEntity action) {
        if(action.extra_data != null) {
            var map = (Map<?,?>) action.extra_data;

            if(map.get("selector_disabled") instanceof Boolean value) {
                return !value;
            }
        }

        return true;
    }

}
