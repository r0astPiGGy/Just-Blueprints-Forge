package com.rodev.jmcgenerator.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class GeneratorEntity {
    public String id;
    public Object schema;

    @Nullable
    public Set<String> ignoreArguments;

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

}
