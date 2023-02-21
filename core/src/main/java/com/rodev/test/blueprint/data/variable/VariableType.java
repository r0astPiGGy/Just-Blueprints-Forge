package com.rodev.test.blueprint.data.variable;

import com.rodev.test.Colors;

public record VariableType(String type, int color) {

    public static VariableType execType() {
        return new VariableType("exec", Colors.WHITE);
    }

}
