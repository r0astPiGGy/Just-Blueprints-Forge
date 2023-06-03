package com.rodev.jbpcore.utils.listener;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ListenerWrapperImpl<T> implements ListenerWrapper<T> {
    private final T listener;

    private String namespace;

    private boolean removeOnNotify = false;

    @Override
    public ListenerWrapper<T> setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    @Override
    public boolean namespaceEquals(String namespaceToCompare) {
        return namespaceToCompare.equals(namespace);
    }

    @Override
    public ListenerWrapper<T> setRemoveOnNotify() {
        removeOnNotify = true;
        return this;
    }

    @Override
    public boolean removeOnNotify() {
        return removeOnNotify;
    }
}
