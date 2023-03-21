package com.rodev.jmcparser.generator;

import com.rodev.jbpcore.blueprint.data.ObjectType;
import com.rodev.jbpcore.blueprint.data.json.ActionEntity;
import com.rodev.jmcgenerator.entity.GeneratorEntity;
import com.rodev.jmcparser.data.action.custom.CustomActionEntity;
import com.rodev.jmcparser.data.game_value.GameValueMappings;
import com.rodev.jmcparser.json.ActionData;
import com.rodev.jmcparser.json.Event;
import com.rodev.jmcparser.json.GameValue;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

        var ignoreArguments = new HashSet<String>();

        ignoreArguments.add("exec");

        // TODO REWORK
        if(action.id.startsWith("set_variable_get")) {
            var output = action.output.get(0);
            builder.append("var $");
            builder.append(output.id);
            builder.append(" = ");
            generatorEntity.output = new HashMap<>(){{
               put(output.id, "$random_var_name");
            }};
        }

        builder.append(data.object);
        builder.append("::");
        builder.append(data.name);

        var selector = findSelector(action);

        if(selector != null) {
            builder.append("<$");
            builder.append(selector.id);
            builder.append(">");
            ignoreArguments.add(selector.id);
        }

        builder.append("($args)");
        if(data.containing != null && data.containing.equals("predicate")) {
            generatorEntity.codeNeedsToBePlaced = false;
        } else {
            //builder.append(";");
        }

        generatorEntity.schema = builder.toString();
        generatorEntity.ignoreArguments = ignoreArguments;

        generatorEntities.add(generatorEntity);
    }

    private boolean isSelector(ActionEntity.PinTypeEntity pin) {
        return isSelector(pin.type);
    }

    private boolean isSelector(String object) {
        return switch (object) {
            case "entity", "player" -> true;
            default -> false;
        };
    }

    @Nullable
    private ActionEntity.PinTypeEntity findSelector(ActionEntity action) {
        return action.input.stream()
                .filter(this::isSelector)
                .findFirst()
                .orElse(null);
    }

    public void onCustomActionInterpreted(CustomActionEntity action) {
        if(action.generator == null)
            throw new IllegalStateException("Custom action by id " + action.id + " doesn't have generator entity.");

        var generator = action.generator;

        generator.id = action.id;

        var ignoreArguments = generator.ignoreArguments;

        if(ignoreArguments == null || ignoreArguments.isEmpty() || !ignoreArguments.contains("exec")) {
            if(ignoreArguments == null) {
                ignoreArguments = new HashSet<>();
            } else {
                ignoreArguments = new HashSet<>(ignoreArguments);
            }
            ignoreArguments.add("exec");
        }

        generator.ignoreArguments = ignoreArguments;

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

        generatorEntity.codeNeedsToBePlaced = false;

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

    public void onCastActionInterpreted(ActionEntity action, ObjectType objectType) {
        var generatorEntity = new GeneratorEntity();
        generatorEntity.id = action.id;

        generatorEntity.schema = List.of(
                "if (variable::is_type($args, \"" + objectType.name() + "\")) {",
                "$cast_success",
                "} else {",
                "$cast_failed",
                "}"
        );
        generatorEntity.output = new HashMap<>(){{
            put("casted_var", "$var_to_cast");
        }};

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
