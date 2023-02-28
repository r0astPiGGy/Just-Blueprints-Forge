package com.rodev.test.blueprint.data;

import com.rodev.test.blueprint.data.action.ActionRegistry;
import com.rodev.test.blueprint.data.action.EnumPinType;
import com.rodev.test.blueprint.data.action.type.ActionTypeRegistry;
import com.rodev.test.blueprint.data.category.ContextCategoryRegistry;
import com.rodev.test.blueprint.data.variable.VariableTypeRegistry;
import com.rodev.test.blueprint.node.NodeView;
import com.rodev.test.blueprint.pin.default_input_value.DefaultBooleanInputView;
import com.rodev.test.blueprint.pin.default_input_value.DefaultEnumInputView;
import com.rodev.test.blueprint.pin.default_input_value.DefaultNumberInputView;
import com.rodev.test.blueprint.pin.default_input_value.DefaultTextInputView;
import com.rodev.test.blueprint.pin.exec_pin.ExecPin;

import java.util.Map;

public class DataInitEventHandler {

    private DataInitEventHandler() {}

    public static void onActionTypeRegistryPreLoad(ActionTypeRegistry registry) {
        registry.addNodeSupplier("event", (color, id, name) -> {
            var node = new NodeView(color, id, name);
            var output = ExecPin.outputPin();

            node.addOutput(output.createRowView());

            return node;
        });
        registry.addNodeSupplier("pure-function", NodeView::new);
    }

    public static void onVariableTypeRegistryPreLoad(VariableTypeRegistry registry) {

    }

    public static void onContextCategoryRegistryPreLoad(ContextCategoryRegistry registry) {

    }

    public static void onActionRegistryPreLoad(ActionRegistry registry) {
        registry.registerPinTypeFactory("enum", (entity, variableType) -> {
            //noinspection unchecked
            Map<String, String> map = (Map<String, String>) entity.extra_data;
            return new EnumPinType(entity.id, entity.label, variableType, map);
        });
    }

    public static void onDataAccessPostLoad(DataAccess dataAccess) {
        VariableTypeRegistry.registerInputPinRowCreatedListener("text", (inputPin, rowView) -> {
            rowView.addDefaultValueView(new DefaultTextInputView());
        });
        VariableTypeRegistry.registerInputPinRowCreatedListener("boolean", (inputPin, rowView) -> {
            rowView.addDefaultValueView(new DefaultBooleanInputView());
        });
        VariableTypeRegistry.registerInputPinRowCreatedListener("number", (inputPin, rowView) -> {
            rowView.addDefaultValueView(new DefaultNumberInputView());
        });
        VariableTypeRegistry.registerInputPinRowCreatedListener("enum", (inputPin, rowView) -> {
            rowView.addDefaultValueView(new DefaultEnumInputView((EnumPinType) inputPin.getType()));
        });
    }

}
