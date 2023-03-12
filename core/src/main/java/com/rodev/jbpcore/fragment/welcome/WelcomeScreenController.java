package com.rodev.jbpcore.fragment.welcome;

import com.rodev.jbpcore.JustBlueprints;
import com.rodev.jbpcore.workspace.Project;
import com.rodev.jbpcore.workspace.WindowManager;
import com.rodev.jbpcore.workspace.Workspace;
import icyllis.modernui.fragment.Fragment;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WelcomeScreenController {

    private final Fragment fragment;

    public void onRecentProjectClicked(Project project) {
        openProject(project);
    }

    public void onProjectCreated(String projectName) {
        Workspace workspace = JustBlueprints.getWorkspace();
        Project project = workspace.createProject(projectName);
        workspace.getProgramData().addRecentProject(projectName);
        workspace.getProgramData().save();

        openProject(project);
    }

    public void openProject(Project project) {
        WindowManager windowManager = JustBlueprints.getWindowManager();

        var editorState = WindowManager.EDITOR_SCREEN;
        editorState.setProjectToView(project);

        windowManager.setState(editorState);

        windowManager.open();
    }

    public void validateProjectName(String name, ValidateResult validateResult) {
        JustBlueprints.getWorkspace().validateProjectName(name, validateResult);
    }

}
