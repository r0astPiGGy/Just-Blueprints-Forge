package com.rodev.jbpcore.data.variable;

import com.rodev.jbpcore.data.DataAccess;
import com.rodev.jbpcore.data.icon.PinIcon;

import java.util.Set;
import java.util.function.Predicate;

public record VariableType(String type, int color, String icon) {

    private static final Set<String> dynamicDeclines = Set.of("exec", "enum", "player", "entity");

    public PinIcon getIcon() {
        return DataAccess.getInstance().iconRegistry.get(icon);
    }

    public Predicate<VariableType> getAcceptsFilter() {
        if(isDynamic()) return s -> true;

        return v -> v.type.equals(type);
    }

    public Predicate<VariableType> getDeclinesFilter() {
        if(isDynamic()) return v -> !dynamicDeclines.contains(v.type);

        return s -> true;
    }

    public boolean isDynamic() {
        return type.equals("dynamic");
    }


}
