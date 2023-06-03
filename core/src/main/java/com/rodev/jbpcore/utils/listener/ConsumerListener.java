package com.rodev.jbpcore.utils.listener;

import java.util.function.Consumer;

public interface ConsumerListener<T> {

    ListenerWrapper<Consumer<T>> addListener(Consumer<T> listener);

    void removeListener(Consumer<T> listener);

    void removeAllByNamespace(String namespace);

    void removeAll();

    static <T> Notifiable<T> create() {
        return new NotifiableConsumerListener<>();
    }

    interface Notifiable<T> extends ConsumerListener<T> {

        void notifyListeners(T object);

    }

}
