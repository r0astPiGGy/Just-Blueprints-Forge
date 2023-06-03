package com.rodev.jbpcore.utils.listener;

public interface ListenerWrapper<T> {

    T getListener();

    ListenerWrapper<T> setNamespace(String namespace);

    boolean namespaceEquals(String namespaceToCompare);

    ListenerWrapper<T> setRemoveOnNotify();

    boolean removeOnNotify();

    static <T> ListenerWrapper<T> wrap(T toWrap) {
        return new ListenerWrapperImpl<>(toWrap);
    }

}
