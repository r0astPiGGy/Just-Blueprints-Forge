package com.rodev.test.blueprint.node;

import java.util.HashMap;
import java.util.Map;

public class NodeDeserializerDeprecated {

    private static final Map<String, Deserializer> deserializers = new HashMap<>();

    public static BPNode deserialize(String deserializerId, Object data) {
        return deserializers.get(deserializerId).deserialize(data);
    }

    public static void register(String deserializerId, Deserializer deserializer) {
        deserializers.put(deserializerId, deserializer);
    }

    public interface Deserializer {

        BPNode deserialize(Object data);

    }

}
