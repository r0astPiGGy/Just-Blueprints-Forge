package com.rodev.jbpcore.blueprint.data.variable;

import com.rodev.jbpcore.Colors;
import com.rodev.jbpcore.blueprint.data.DataAccess;
import com.rodev.jbpcore.blueprint.data.icon.PinIcon;

public record VariableType(String type, int color, String icon) {

    public static VariableType execType() {
        return new VariableType("exec", Colors.WHITE, "exec");
    }

    public PinIcon getIcon() {
        return DataAccess.getInstance().iconRegistry.get(icon);
    }

}
