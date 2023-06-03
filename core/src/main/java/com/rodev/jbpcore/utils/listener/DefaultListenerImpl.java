package com.rodev.jbpcore.utils.listener;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class DefaultListenerImpl implements DefaultListener.Notifiable {

    private final Set<Callable> listeners = new HashSet<>();

    private void listenerNotNull(Callable listener) {
        Objects.requireNonNull(listener, "Listener cannot be null");
    }

    @Override
    public void addListener(Callable listener) {
        listenerNotNull(listener);

        if(listeners.contains(listener))
            throw new IllegalArgumentException("This listener already added");

    }

    @Override
    public void removeListener(Callable listener) {
        listenerNotNull(listener);

        listeners.remove(listener);
    }

    @Override
    public void removeAll() {
        listeners.clear();
    }

    @Override
    public void notifyListeners() {
        listeners.forEach(Callable::call);
    }
}
