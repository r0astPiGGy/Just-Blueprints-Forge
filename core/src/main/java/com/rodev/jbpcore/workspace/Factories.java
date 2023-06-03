package com.rodev.jbpcore.workspace;

public class Factories {

    private static WindowFactory windowFactory;

    public static void setWindowFactory(WindowFactory windowFactory) {
        Factories.windowFactory = windowFactory;
    }

    public static WindowFactory getWindowFactory() {
        return windowFactory;
    }

}
