package com.rodev.jbpcore.handlers;

import com.rodev.jbpcore.data.action.ActionRegistry;
import com.rodev.jbpcore.data.action.pin_type.EnumPinType;
import com.rodev.jbpcore.data.action.pin_type.PinType;
import com.rodev.jbpcore.data.json.ActionEntity;
import com.rodev.jbpcore.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.dynamic.DynamicDependency;
import com.rodev.jbpcore.blueprint.pin.dynamic.DynamicPinType;
import com.rodev.jbpcore.blueprint.pin.exec_pin.ExecPinType;
import com.rodev.jbpcore.blueprint.pin.list_pin.ListPinType;
import com.rodev.jbpcore.blueprint.pin.list_pin.dynamic.DynamicListPinType;
import com.rodev.jbpcore.blueprint.pin.map_pin.MapPinType;
import com.rodev.jbpcore.blueprint.pin.map_pin.dynamic.DynamicMapPinType;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor(staticName = "of")
public class PinTypeFactoryHandler {

    private final ActionRegistry registry;

    public void register() {
        registry.registerPinTypeFactory("exec", this::createExecPinType);
        registry.registerPinTypeFactory("enum", this::createEnumPinType);
        registry.registerPinTypeFactory("dynamic", this::createDynamicPinType);
        registry.registerPinTypeFactory("list", this::createListPinType);
        registry.registerPinTypeFactory("dictionary", this::createMapPinType);
    }

    private Map<String, Object> getExtraData(Object object) {
        //noinspection unchecked
        return (Map<String, Object>) object;
    }

    private PinType createExecPinType(ActionEntity.PinTypeEntity entity, VariableType variableType) {
        return new ExecPinType(entity.id, entity.label, variableType);
    }

    private PinType createEnumPinType(ActionEntity.PinTypeEntity entity, VariableType variableType) {
        //noinspection unchecked
        var map = (Map<String,String>) entity.extra_data;
        return new EnumPinType(entity.id, entity.label, variableType, map);
    }

    private PinType createDynamicPinType(ActionEntity.PinTypeEntity entity, VariableType variableType) {
        if(!variableType.isDynamic()) {
            throw new IllegalStateException("VariableType should be dynamic!");
        }

        DynamicDependency typeDependency = getDynamicDependencyInExtraData(getExtraData(entity.extra_data), "type");

        return new DynamicPinType(entity.id, entity.label, variableType, typeDependency);
    }

    private PinType createListPinType(ActionEntity.PinTypeEntity entity, VariableType variableType) {
        if(entity.extra_data == null) {
            throw new IllegalStateException("Element type of this list not found (" + entity.id +")");
        }

        var map = getExtraData(entity.extra_data);
        var elementTypeId = (String) map.getOrDefault("element-type", "variable");

        var elementType = registry.getVariableType(elementTypeId);

        if(elementType.isDynamic()) {
            DynamicDependency elementDep = getDynamicDependencyInExtraData(map, "element-type");

            return new DynamicListPinType(entity.id, entity.label, variableType, elementType, elementDep);
        }

        return new ListPinType(entity.id, entity.label, variableType, elementType);
    }

    private PinType createMapPinType(ActionEntity.PinTypeEntity entity, VariableType variableType) {
        if(entity.extra_data == null) {
            throw new IllegalStateException("Extra data for dictionary is required (" + entity.id +")");
        }

        var map = getExtraData(entity.extra_data);
        var keyTypeId = (String) map.getOrDefault("key-type", "variable");
        var valueTypeId = (String) map.getOrDefault("value-type", "variable");

        var keyType = registry.getVariableType(keyTypeId);
        var valueType = registry.getVariableType(valueTypeId);

        boolean keyTypeDynamic = keyType.isDynamic();
        boolean valueTypeDynamic = valueType.isDynamic();
        boolean typeDynamic = keyTypeDynamic || valueTypeDynamic;

        if(typeDynamic) {
            DynamicDependency keyTypeDep = null;
            DynamicDependency valueTypeDep = null;
            if(keyTypeDynamic) {
                keyTypeDep = getDynamicDependencyInExtraData(map, "key-type");
            }
            if(valueTypeDynamic) {
                valueTypeDep = getDynamicDependencyInExtraData(map, "value-type");
            }

            return new DynamicMapPinType(entity.id, entity.label, variableType, keyType, valueType, keyTypeDep, valueTypeDep);
        }

        return new MapPinType(entity.id, entity.label, variableType, keyType, valueType);
    }

    @Nullable
    private DynamicDependency getDynamicDependencyInExtraData(Map<String, Object> extraData, String objectTypeId) {
        if(extraData == null) return null;

        //noinspection unchecked
        var dependencies = (Map<String, Object>) extraData.get("dynamic-dependencies");

        if(dependencies == null) return null;

        //noinspection unchecked
        var dependency = (Map<String,String>) dependencies.get(objectTypeId);

        if(dependency == null) return null;

        var dependsOn = dependency.get("dest-pin-id");
        var destType = dependency.get("dest-type");

        Objects.requireNonNull(dependsOn);
        Objects.requireNonNull(destType);

        return new DynamicDependency(dependsOn, destType);
    }
}
