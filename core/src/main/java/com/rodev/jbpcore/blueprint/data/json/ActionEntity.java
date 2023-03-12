package com.rodev.jbpcore.blueprint.data.json;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class ActionEntity {
    public String id;
    public String type;
    public String name;
    public String category;
    public List<PinTypeEntity> input;
    public List<PinTypeEntity> output;
    public Object extra_data;
    public String icon_namespace;

    @NoArgsConstructor
    @AllArgsConstructor
    public static class PinTypeEntity {
        public String id;
        public String label;
        public String type;
        public Object extra_data;
    }
}

