package com.rodev.jbpcore.data.variable;

import com.rodev.jbpcore.data.Registry;
import com.rodev.jbpcore.data.json.VariableTypeEntity;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.ui.pin.PinRowView;
import com.rodev.jbpcore.utils.StringParse;
import icyllis.modernui.graphics.Color;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class VariableTypeRegistry extends Registry<String, VariableType> {

    // TODO: Rename
    private static final Map<String, OnInputPinRowCreatedListener> inputPinRowCreatedListeners = new HashMap<>();

    public static void registerInputPinRowCreatedListener(String pinType, OnInputPinRowCreatedListener listener) {
        inputPinRowCreatedListeners.put(pinType, listener);
    }

    public static RegistryListenersBuilder inputPinRowCreatedListenerBuilder() {
        return new RegistryListenersBuilder();
    }

    public static void onPinRowViewCreated(Pin pin, PinRowView rowView) {
        var type = pin.getVariableType();

        var listener = inputPinRowCreatedListeners.get(type.type());

        if(listener == null) return;

        listener.onPinRowCreated(pin, rowView);
    }


    public void load(VariableTypeEntity[] variableTypeList) {
        data.clear();
        Arrays.stream(variableTypeList).map(this::create).forEach(this::add);
    }

    private VariableType create(VariableTypeEntity entity) {
        var rawColor = entity.color.replace(" ", "");
        var rawRgb = rawColor.split(",");
        var rgb = new Integer[rawRgb.length];
        StringParse.parseAll(rawRgb, rgb, Integer::parseInt);

        var color = Color.rgb(rgb[0], rgb[1], rgb[2]);

        return new VariableType(entity.id, color, entity.icon);
    }

    private void add(VariableType variableType) {
        data.put(variableType.type(), variableType);
    }

    public static class RegistryListenersBuilder {

        private final Map<String, OnInputPinRowCreatedListener> listenerMap = new HashMap<>();

        private RegistryListenersBuilder() {}

        public RegistryListenersBuilder addListener(String pinType, OnInputPinRowCreatedListener listener) {
            listenerMap.put(pinType, listener);
            return this;
        }

        public void registerAll() {
            listenerMap.forEach(VariableTypeRegistry::registerInputPinRowCreatedListener);
        }

    }

}
