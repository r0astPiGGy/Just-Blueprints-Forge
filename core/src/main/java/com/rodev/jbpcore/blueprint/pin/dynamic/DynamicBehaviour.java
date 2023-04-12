package com.rodev.jbpcore.blueprint.pin.dynamic;

import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface DynamicBehaviour {

    void onTypeSet(VariableType variableType);

    void onTypeReset();

    @NotNull
    DynamicPinDestination getDestination();

    static DynamicBehaviour of(@NotNull DynamicPinDestination destination, @NotNull Consumer<VariableType> typeSetter, @NotNull Runnable onTypeReset) {
        return new Impl(destination, typeSetter, onTypeReset);
    }

    @RequiredArgsConstructor
    class Impl implements DynamicBehaviour {

        @Getter
        private final DynamicPinDestination destination;
        private final Consumer<VariableType> onTypeSet;
        private final Runnable onTypeReset;

        @Override
        public void onTypeSet(VariableType variableType) {
            onTypeSet.accept(variableType);
        }

        @Override
        public void onTypeReset() {
            onTypeReset.run();
        }
    }
}
