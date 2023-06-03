package com.rodev.jbpcore;

import com.rodev.jbpcore.ui.fragment.editor.EditorEventListener;
import com.rodev.jbpcore.workspace.ModernUIWindowManager;
import com.rodev.jbpcore.workspace.Workspace;
import org.jetbrains.annotations.NotNull;

public class JustBlueprints {

    private static Workspace workspace;

    private static ModernUIWindowManager windowManager;
    private static EditorEventListener editorEventListener = new EditorEventListener() {};

    public static void setWorkspace(Workspace workspace) {
        if(JustBlueprints.workspace != null) {
            throw new IllegalStateException("Workspace is already initialized.");
        }

        JustBlueprints.workspace = workspace;
        workspace.reloadProjects();
    }

    public static void setWindowManager(ModernUIWindowManager windowManager) {
        if(JustBlueprints.windowManager != null) {
            throw new IllegalStateException("Window manager is already initialized.");
        }

        JustBlueprints.windowManager = windowManager;
    }

    public static void setEditorEventListener(@NotNull EditorEventListener editorEventListener) {
        JustBlueprints.editorEventListener = editorEventListener;
    }

    public static EditorEventListener getEditorEventListener() {
        return editorEventListener;
    }

    public static ModernUIWindowManager getWindowManager() {
        return windowManager;
    }

    public static Workspace getWorkspace() {
        return workspace;
    }

}
