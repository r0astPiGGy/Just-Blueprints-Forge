package com.rodev.jbpcore.blueprint.pin.dynamic;

import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.dynamic.impl.DynamicGroupImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface Dynamic extends Pin {

    /**
     * @return Dynamic Pin Group, with root == this
     */
    default DynamicGroup createDynamicGroup() {
        return DynamicGroup.of(this, DynamicBehaviour.of(
                DynamicPinDestination.TYPE,
                this::setVariableType,
                this::resetVariableType
        ));
    }

    /**
     * Вызывается после динамического соединения.
     * Возвращает affected Destinations. Если этот Pin является root, то он возвращает соответствующие destinations,
     * куда VariableType(s) были установлены.
     * Контракт: аргумент connection всегда одного типа, что и этот Pin.
     */
    Map<DynamicPinDestination, VariableType> getAffectedDestinationsFromConnection(Pin connection);

    boolean isDynamicRoot();

    void onAddToGroupDelegate(DynamicGroupResolver resolver);

    void setVariableType(@NotNull VariableType variableType);

    /**
     * @return if this dynamic pin's VariableType got changed after connection
     */
    boolean isDynamicVariableSet();

    void resetVariableType();

}
