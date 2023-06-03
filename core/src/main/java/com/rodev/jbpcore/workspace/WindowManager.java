package com.rodev.jbpcore.workspace;

public abstract class WindowManager {

    public static final WindowState.WelcomeWindowState WELCOME_SCREEN = new WindowState.WelcomeWindowState();
    public static final WindowState.EditorWindowState EDITOR_SCREEN = new WindowState.EditorWindowState();

    protected WindowState state = WELCOME_SCREEN;

    public void open() {
        state.createWindow().onOpen();
    }

    public void onTransactionFrom(Window window) {
        state.createWindow().onTransaction(window);
    }

    public void setState(WindowState state) {
        this.state = state;
    }
}
