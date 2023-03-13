package com.rodev.jmcparser.data.event;

import com.rodev.jmcparser.json.Event;
import com.rodev.jmcparser.json.GameValue;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.Predicate;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class EventOutputFiller {

    private final Event[] events;
    private final GameValue[] gameValues;

    private final Map<String, Event> eventsById = new HashMap<>();
    private final Map<String, List<String>> eventGroupsById = new HashMap<>();
    private final Map<String, String> mappedEventsByLocalizationId = new HashMap<>();

    public static void fill(Event[] events, GameValue[] gameValues) {
        new EventOutputFiller(events, gameValues).fill();
    }

    public void fill() {
        mapEvents();
        mapEventGroups();
        mapEventsByLocalizationId();

        for(var gameValue : gameValues) {
            if(!gameValue.id.startsWith("event")) continue;

            if(gameValue.worksWith == null) continue;

            getApplicableEventsForValues(gameValue.worksWith)
                    .stream()
                    .distinct()
                    .forEach(e -> e.rawOutput.add(gameValue));
        }
    }

    private void mapEvents() {
        System.out.println("Querying " + events.length + " events...");
        for (Event event : events) {
            eventsById.put(event.id, event);
        }

        System.out.println("Mapped events [" + eventsById.size() + "]: " + String.join(", ", eventsById.keySet()));
    }

    private void mapEventGroups() {
        final var map = eventGroupsById;

        map.put("piston_events", filterEventsByPredicate(id -> id.contains("piston")));
        map.put("interact_events", new LinkedList<>(){{
            add("player_interact");
            addAll(filterEventsByPredicate(id -> id.contains("click") && !id.contains("inventory")));
        }});
        map.put("place_break_events", List.of(
                "player_place_block",
                "player_break_block"
        ));
        map.put("world_block_events", filterEventsByPredicate(id -> id.contains("block")));
        map.put("explosion_events", List.of("block_explode"));
        map.put("item_events", filterEventsByPredicate(id -> id.contains("item")));
        map.put("death_events", filterEventsByPredicate(id -> id.contains("death")));
    }

    private List<String> filterEventsByPredicate(Predicate<String> predicate) {
        return eventsById.keySet().stream().filter(predicate).toList();
    }

    private void mapEventsByLocalizationId() {
        final var map = mappedEventsByLocalizationId;

        map.put("player_shoot_bow", "player_shot_bow");
        map.put("player_own_inventory_click", "player_click_own_inventory");
        map.put("player_other_inventory_click", "player_click_inventory");
    }

    private List<Event> getApplicableEventsForValues(String[] worksWith) {
        var list = new LinkedList<Event>();
        for (String eventId : worksWith) {
            list.addAll(getEventsById(eventId));
        }

        return list;
    }

    private List<Event> getEventGroupById(String id) {
        var group = eventGroupsById.get(id);
        if(group == null) {
            System.out.println("Event group by outputPin " + id + " not found.");
            return Collections.emptyList();
        }

        return group.stream().map(eventsById::get).toList();
    }

    private List<Event> getEventsById(String id) {
        if(id.endsWith("_events")) {
            return getEventGroupById(id);
        }

        var eventId = id.replace("_event", "");
        var ev = eventsById.get(eventId);

        if(ev != null) return List.of(ev);

        var tempId = "player_" + eventId;
        ev = eventsById.get(tempId);

        if(ev != null) return List.of(ev);

        tempId = mappedEventsByLocalizationId.get(eventId);
        ev = eventsById.get(tempId);

        if(ev != null) return List.of(ev);

        System.out.println("Event by outputPin " + eventId + " not found. Is it group?");

        return List.of();
    }

}
