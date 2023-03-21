package com.rodev.jmcparser.data.action;

import com.rodev.jbpcore.blueprint.data.json.ActionEntity;
import com.rodev.jmcparser.data.Interpreter;
import com.rodev.jmcparser.data.LocaleProvider;
import com.rodev.jmcparser.data.category.CategoryProvider;
import com.rodev.jmcparser.json.ActionData;
import com.rodev.jmcparser.json.ActionDataArgument;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class ActionInterpreter extends Interpreter<ActionData> implements ActionNameHandler {

    private final Set<String> variableTypes = new HashSet<>();
    private final Set<String> withoutCategory = new HashSet<>();
    private final LocaleProvider localeProvider;
    private final CategoryProvider categoryProvider;

    @Setter
    private ActionTypeHandler actionTypeHandler = data -> "function";

    @Setter
    private ActionNameHandler actionNameHandler = this;

    @Setter
    private PinTypeNameHandler pinTypeNameHandler = typeId -> typeId;

    public ActionInterpreter(ActionData[] data, LocaleProvider localeProvider, CategoryProvider categoryProvider) {
        super(data);
        this.localeProvider = localeProvider;
        this.categoryProvider = categoryProvider;
    }

    @Override
    public ActionEntity[] interpret() {
        var a = super.interpret();

        System.out.println("Variable types [" + variableTypes.size() + "]: " + String.join(", ", variableTypes));
        System.out.println("Actions without category [" + withoutCategory.size() + "]:" + String.join(", ", withoutCategory));

        return a;
    }

    @Override
    protected @Nullable ActionEntity interpret(@NotNull ActionData actionData) {
        var actionEntity = new ActionEntity();

        actionEntity.id = actionData.id;
        actionEntity.type = actionTypeHandler.handleActionType(actionData);
        actionEntity.name = actionNameHandler.handleActionName(actionData, localeProvider);
        actionEntity.category = getCategoryFor(actionData);
        actionEntity.input = createInput(actionEntity, actionData);
        actionEntity.output = createOutput(actionEntity, actionData);
        actionEntity.icon_namespace = "actions";

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

    private List<ActionEntity.PinTypeEntity> createInput(ActionEntity action, ActionData actionData) {
        var input = new LinkedList<ActionEntity.PinTypeEntity>();

        if (action.type.equalsIgnoreCase("function")) {
            input.add(createExec());
        }

        var id = actionData.id;
        var isVariableSetter = id.startsWith("set_variable");
        if (!isVariableSetter) {
            var inputPin = createObjectInputPin(actionData);

            if (inputPin != null) {
                input.add(inputPin);
            }
        }

        int i = 0;

        var isVariableGetter = id.startsWith("set_variable_get") && actionData.assigning != null;
        if (isVariableGetter)
            i = 1; // Skip first arg

        for (; i < actionData.args.length; i++) {
            var arg = actionData.args[i];

            if (arg == null) continue;

            input.add(createInputPin(actionData, arg));
        }

        return input;
    }

    private List<ActionEntity.PinTypeEntity> createOutput(ActionEntity action, ActionData actionData) {
        var variableGetter = actionData.id.startsWith("set_variable_get");
        var predicate = isPredicate(actionData);

        var output = new LinkedList<ActionEntity.PinTypeEntity>();

        if(action.type.equalsIgnoreCase("function")) {
            output.add(createExec());
        }

        if(!predicate && !variableGetter) {
            return output;
        }

        var type = "variable";

        if(predicate) {
            type = "condition";
        }

        var pinTypeEntity = new ActionEntity.PinTypeEntity();
        pinTypeEntity.label = "Return value";
        pinTypeEntity.type = type;
        pinTypeEntity.id = "-";

        return List.of(pinTypeEntity);
    }

    private boolean isPredicate(ActionData actionData) {
        if(actionData.containing != null) {
            return actionData.containing.equalsIgnoreCase("predicate");
        }

        return false;
    }

    private ActionEntity.PinTypeEntity createObjectInputPin(ActionData actionData) {
        switch (actionData.object) {
            case "code":
            case "world":
            case "select":
            case "repeat":
                return null;
        }

        if(actionData.object.equalsIgnoreCase("variable") && isPredicate(actionData))
            return null;

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
}
