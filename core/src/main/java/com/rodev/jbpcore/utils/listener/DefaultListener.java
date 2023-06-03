package com.rodev.jbpcore.utils.listener;

public interface DefaultListener {

    void addListener(Callable listener);

    void removeListener(Callable listener);

    void removeAll();

    static Notifiable create() {
        return new DefaultListenerImpl();
    }

    interface Notifiable extends DefaultListener {

        void notifyListeners();

    }

}
