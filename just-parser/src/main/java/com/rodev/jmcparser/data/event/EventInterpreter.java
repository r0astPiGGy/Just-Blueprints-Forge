package com.rodev.jmcparser.data.event;

import com.rodev.jbpcore.data.json.ActionEntity;
import com.rodev.jbpcore.data.json.EventEntity;
import com.rodev.jmcparser.data.Interpreter;
import com.rodev.jmcparser.data.LocaleProvider;
import com.rodev.jmcparser.json.Event;
import com.rodev.jmcparser.json.GameValue;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class EventInterpreter extends Interpreter<Event> implements EventNameHandler {

    @Setter
    private EventNameHandler eventNameHandler = this;

    private final LocaleProvider localeProvider;

    protected EventInterpreter(Event[] data, LocaleProvider localeProvider) {
        super(data);
        this.localeProvider = localeProvider;
    }

    @Override
    protected @Nullable ActionEntity interpret(@NotNull Event event) {
        var actionEntity = EventEntity.create(event.cancellable);

        actionEntity.id = event.id;
        actionEntity.name = eventNameHandler.handleEventName(event, localeProvider);
        if(event.category == null) {
            actionEntity.category = "events";
        } else {
            actionEntity.category = String.format("%s.%s-%s", "events", "events", event.category);
        }
        actionEntity.input = Collections.emptyList();
        actionEntity.output = createOutput(event);
        actionEntity.icon_namespace = "events";

        return actionEntity;
    }

    private List<ActionEntity.PinTypeEntity> createOutput(Event event) {
        var output = new LinkedList<ActionEntity.PinTypeEntity>();

        output.add(createExec());

        event.rawOutput.stream().map(this::createOutput).forEach(output::add);

        return output;
    }

    private ActionEntity.PinTypeEntity createOutput(GameValue gameValue) {
        var pin = new ActionEntity.PinTypeEntity();

        pin.id = gameValue.id;
        pin.type = gameValue.type;
        pin.label = gameValue.name;

        applyExtraDataToGameValue(pin, gameValue);

        return pin;
    }

    @Override
    public String handleEventName(Event data, LocaleProvider localeProvider) {
        var key = "creative_plus.trigger." + data.id + ".name";

        return localeProvider.translateKeyOrDefault(key);
    }
}
