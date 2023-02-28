package com.rodev.jmcparser.data;

import com.rodev.jmcparser.json.ActionData;
import com.rodev.jmcparser.json.ActionDataArgument;
import com.rodev.jmcparser.util.TimeCounter;
import com.rodev.test.blueprint.data.json.ActionEntity;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.function.Function;

@RequiredArgsConstructor
public class DataInterpreter implements ActionNameHandler {

    private final Set<String> variableTypes = new HashSet<>();
    private final Set<String> withoutCategory = new HashSet<>();
    private final ActionData[] data;
    private final LocaleProvider localeProvider;
    private final CategoryProvider categoryProvider;

    @Setter
    private ActionTypeHandler actionTypeHandler = data -> "function";

    @Setter
    private ActionNameHandler actionNameHandler = this;

    @Setter
    private PinTypeNameHandler pinTypeNameHandler = typeId -> typeId;


    public ActionEntity[] interpret() {
        var counter = new TimeCounter();

        var array = new ActionEntity[data.length];

        for(int i = 0; i < data.length; i++) {
            var actionData = data[i];

            if(actionData == null) continue;

            array[i] = create(actionData);
        }

        counter.print(ms -> "Interpreted " + array.length + " actions in " + ms + "ms.");
        System.out.println("Variable types [" + variableTypes.size() + "]: " + String.join(", ", variableTypes));
        System.out.println("Actions without category [" + withoutCategory.size() + "]:" + String.join(", ", withoutCategory));

        return array;
    }

    private ActionEntity create(ActionData actionData) {
        var actionEntity = new ActionEntity();

        actionEntity.id = actionData.id;
        actionEntity.type = actionTypeHandler.handleActionType(actionData);
        actionEntity.name = actionNameHandler.handleActionName(actionData, localeProvider);
        actionEntity.category = getCategoryFor(actionData);
        actionEntity.input = createInput(actionData);
        actionEntity.output = createOutput(actionData);

        return actionEntity;
    }

    private String getCategoryFor(ActionData actionData) {
        var id = actionData.id;
        var base = categoryProvider.getCategoryForActionId(id);

        if(base != null) return base;

        if(id.startsWith("if_game")) return "if_game";

        if(id.startsWith("game")) return "game_action";

        withoutCategory.add(id);

        return "misc";
    }

    @Override
    public String handleActionName(ActionData data, LocaleProvider localeProvider) {
        var key = "creative_plus.action." + data.id + ".name";

        return localeProvider.translateKeyOrDefault(key);
    }

    private List<ActionEntity.PinTypeEntity> createInput(ActionData actionData) {
        var input = new LinkedList<ActionEntity.PinTypeEntity>();

        var inputPin = createObjectInputPin(actionData);

        if(inputPin != null) {
            input.add(inputPin);
        }


        for(var arg : actionData.args) {
            if(arg == null) continue;

            input.add(createInputPin(actionData, arg));
        }

        return input;
    }

    private ActionEntity.PinTypeEntity createObjectInputPin(ActionData actionData) {
        switch (actionData.object) {
            case "code":
            case "world":
            case "repeat":
                return null;
        }

        var pin = new ActionEntity.PinTypeEntity();

        pin.type = actionData.object;
        pin.label = pinTypeNameHandler.handlePinTypeName(actionData.object);
        pin.id = actionData.object;

        if(pin.type != null) {
            variableTypes.add(pin.type);
        }

        return pin;
    }

    private ActionEntity.PinTypeEntity createInputPin(ActionData actionData, ActionDataArgument arg) {
        var pin = new ActionEntity.PinTypeEntity();

        if(arg._enum != null) {
            pin.extra_data = generateEnumValues(arg._enum, rawEnumValue -> {
                var localeId =
                        String.format("creative_plus.action.%s.argument.%s.enum.%s.name",
                        actionData.id, arg.name, rawEnumValue.toLowerCase());

                return localeProvider.translateKeyOrDefault(localeId);
            });
        }

        pin.type = arg.type;
        pin.id = arg.name;

        if(pin.type != null) {
            variableTypes.add(pin.type);
        } else {
            pin.type = "variable";
        }

        var localeId = String.format("creative_plus.action.%s.argument.%s.name", actionData.id, arg.name);

        pin.label = localeProvider.translateKeyOrDefault(localeId);

        return pin;
    }

    private Map<String, String> generateEnumValues(String[] rawValues, Function<String, String> translationSupplier) {
        var map = new HashMap<String, String>();

        for(String rawValue : rawValues) {
            String translation = translationSupplier.apply(rawValue);

            map.put(rawValue, translation);
        }

        return map;
    }

    private List<ActionEntity.PinTypeEntity> createOutput(ActionData actionData) {
        if(actionData.containing == null && actionData.assigning == null) {
            return Collections.emptyList();
        }

        var type = "variable";

        if (actionData.containing != null && actionData.containing.equalsIgnoreCase("predicate")) {
            type = "boolean";
        } else {
            return Collections.emptyList();
        }

        var pinTypeEntity = new ActionEntity.PinTypeEntity();
        pinTypeEntity.label = "Return value";
        pinTypeEntity.type = type;
        pinTypeEntity.id = "-";

        return List.of(pinTypeEntity);
    }
}
