package com.rodev.test;

import com.rodev.test.workspace.WindowManager;
import com.rodev.test.workspace.Workspace;

public class JustBlueprints {

    private static Workspace workspace;

    private static WindowManager windowManager;

    public static final String VERSION = "0.1-indev";

    public static void setWorkspace(Workspace workspace) {
        if(JustBlueprints.workspace != null) {
            throw new IllegalStateException("Workspace is already initialized.");
        }

        JustBlueprints.workspace = workspace;
        workspace.reloadProjects();
    }

    public static void setWindowManager(WindowManager windowManager) {
        if(JustBlueprints.windowManager != null) {
            throw new IllegalStateException("Window manager is already initialized.");
        }

        JustBlueprints.windowManager = windowManager;
    }

    public static WindowManager getWindowManager() {
        return windowManager;
    }

    public static Workspace getWorkspace() {
        return workspace;
    }

}
