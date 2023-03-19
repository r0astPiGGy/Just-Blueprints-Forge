package com.rodev.jmcgenerator.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class GeneratorEntity implements Cloneable {
    public String id;
    public Object schema;

    @Nullable
    public Set<String> ignoreArguments;

    @JsonAlias("place-code")
    public boolean codeNeedsToBePlaced = true;

    @Nullable
    public String type;

    @Nullable
    public Map<String, String> output;

    @SuppressWarnings("unchecked")
    @JsonIgnore
    public List<String> getSchemaLines() {
        if(schema instanceof String str) {
            return List.of(str);
        }

        return (List<String>) schema;
    }

    @JsonIgnore
    public boolean isEvent() {
        return type != null && type.equals("root");
    }

    @JsonIgnore
    public boolean shouldIgnoreArgumentById(String id) {
        return ignoreArguments != null && ignoreArguments.contains(id);
    }

    @Override
    public GeneratorEntity clone() {
        try {
            return (GeneratorEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
