package com.rodev.jbpcore.blueprint.data.variable;

import com.rodev.jbpcore.Colors;
import com.rodev.jbpcore.blueprint.data.DataAccess;
import com.rodev.jbpcore.blueprint.data.icon.PinIcon;

import java.util.Objects;

public class VariableType {
    private final String type;
    private final int color;
    private final String icon;

    public VariableType(String type, int color, String icon) {
        this.type = type;
        this.color = color;
        this.icon = icon;
    }

    public static VariableType execType() {
        return new VariableType("exec", Colors.WHITE, "exec");
    }

    public PinIcon getIcon() {
        return DataAccess.getInstance().iconRegistry.get(icon);
    }

    public String type() {
        return type;
    }

    public int color() {
        return color;
    }

    public String icon() {
        return icon;
    }

    public boolean isDynamic() {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (VariableType) obj;
        return Objects.equals(this.type, that.type) &&
                this.color == that.color &&
                Objects.equals(this.icon, that.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, color, icon);
    }

    @Override
    public String toString() {
        return "VariableType[" +
                "type=" + type + ", " +
                "color=" + color + ", " +
                "icon=" + icon + ']';
    }


}
