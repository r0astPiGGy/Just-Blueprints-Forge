package com.rodev.test.blueprint.data.json;

import java.util.List;

public class ActionEntity {
    public String id;
    public String type;
    public String name;
    public String category;
    public List<PinTypeEntity> input;
    public List<PinTypeEntity> output;

    public static class PinTypeEntity {
        public String id;
        public String label;
        public String type;
    }
}

