package com.rodev.test;

import com.rodev.test.workspace.Workspace;

public class JustBlueprints {

    private static Workspace workspace;

    public static final String VERSION = "0.1-indev";

    public static void setWorkspace(Workspace workspace) {
        if(JustBlueprints.workspace != null) {
            throw new IllegalStateException("Already initialized");
        }

        JustBlueprints.workspace = workspace;
    }

    public static Workspace getWorkspace() {
        return workspace;
    }

}
