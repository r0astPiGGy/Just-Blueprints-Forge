package com.rodev.jbpcore.workspace;

public interface Window {

    void onOpen();

    void onTransaction(Window previousDestination);

}
