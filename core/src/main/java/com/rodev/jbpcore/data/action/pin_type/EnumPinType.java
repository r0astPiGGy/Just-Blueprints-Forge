package com.rodev.jbpcore.data.action.pin_type;

import com.rodev.jbpcore.data.action.EnumValue;
import com.rodev.jbpcore.data.variable.VariableType;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class EnumPinType extends PinType {

    private final Map<String, String> enumeration;
    private final List<EnumValue> values;

    public EnumPinType(String id, String name, VariableType type, Map<String, String> enumeration) {
        super(id, name, type);
        this.enumeration = enumeration;

        values = enumeration.entrySet()
                .stream()
                .map(entry -> new EnumValue(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public Map<String, String> enumeration() {
        return enumeration;
    }

    public List<EnumValue> values() {
        return values;
    }

    @Override
    public String getType() {
        return "enum";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (EnumPinType) obj;
        return Objects.equals(this.getId(), that.getId()) &&
                Objects.equals(this.getName(), that.getName()) &&
                Objects.equals(this.getVariableType(), that.getVariableType()) &&
                Objects.equals(this.enumeration(), that.enumeration());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getVariableType(), enumeration());
    }

    @Override
    public String toString() {
        return "EnumPinType[" +
                "outputPin=" + getId() + ", " +
                "name=" + getName() + ", " +
                "type=" + getVariableType() + ", " +
                "enumeration=" + enumeration() + ']';
    }
}
