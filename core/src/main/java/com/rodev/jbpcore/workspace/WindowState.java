package com.rodev.jbpcore.workspace;

public interface WindowState {

    Window createWindow();

    class EditorWindowState implements WindowState {

        private Project project;

        public void setProject(Project project) {
            this.project = project;
        }

        @Override
        public Window createWindow() {
            return Factories.getWindowFactory().createEditorWindow(project);
        }

    }

    class WelcomeWindowState implements WindowState {
        @Override
        public Window createWindow() {
            return Factories.getWindowFactory().createWelcomeWindow();
        }
    }

}
