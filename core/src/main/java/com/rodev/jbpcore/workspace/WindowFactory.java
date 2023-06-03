package com.rodev.jbpcore.workspace;

public interface WindowFactory {

    Window createEditorWindow(Project projectToView);

    Window createWelcomeWindow();

}
