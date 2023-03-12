package com.rodev.jbpcore.blueprint.data.variable;

import com.rodev.jbpcore.Colors;

public record VariableType(String type, int color) {

    public static VariableType execType() {
        return new VariableType("exec", Colors.WHITE);
    }

}
