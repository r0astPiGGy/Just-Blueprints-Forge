package com.rodev.jbpcore.utils.listener;

import java.util.*;
import java.util.function.Consumer;

class NotifiableConsumerListener<T> implements ConsumerListener.Notifiable<T> {

    private final Map<Consumer<T>, ListenerWrapper<Consumer<T>>> listeners = new HashMap<>();

    private void listenerNotNull(Consumer<T> listener) {
        Objects.requireNonNull(listener, "Listener cannot be null");
    }

    @Override
    public ListenerWrapper<Consumer<T>> addListener(Consumer<T> listener) {
        listenerNotNull(listener);

        if(listeners.containsKey(listener))
            throw new IllegalArgumentException("This listener already added");

        var wrapper = ListenerWrapper.wrap(listener);

        listeners.put(listener, wrapper);

        return wrapper;
    }

    @Override
    public void removeListener(Consumer<T> listener) {
        listenerNotNull(listener);

        listeners.remove(listener);
    }

    @Override
    public void removeAllByNamespace(String namespace) {
        listeners.values()
                .stream()
                .filter(w -> w.namespaceEquals(namespace))
                .toList()
                .forEach(this::removeWrapper);
    }

    @Override
    public void removeAll() {
        listeners.clear();
    }

    @Override
    public void notifyListeners(T object) {
        listeners.values()
                .stream() // Avoid ConcurrentModificationException
                .toList()
                .forEach(w -> onIterate(w, object));
    }

    private void onIterate(ListenerWrapper<Consumer<T>> wrapper, T object) {
        var listener = wrapper.getListener();

        listener.accept(object);

        if(wrapper.removeOnNotify()) {
            removeWrapper(wrapper);
        }
    }

    private void removeWrapper(ListenerWrapper<Consumer<T>> wrapper) {
        listeners.remove(wrapper.getListener());
    }
}
