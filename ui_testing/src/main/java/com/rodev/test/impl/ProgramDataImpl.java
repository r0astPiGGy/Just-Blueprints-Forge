package com.rodev.test.impl;

import com.rodev.test.workspace.ProgramData;
import com.rodev.test.workspace.Project;
import com.rodev.test.workspace.Workspace;
import com.rodev.test.workspace.impl.ProjectImpl;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class ProgramDataImpl implements ProgramData {
    private final List<Project> recentProjects = new LinkedList<>();

    public ProgramDataImpl(WorkspaceImpl workspace) {
        for(int i = 0; i < 3; i++) {
            recentProjects.add(workspace.createProject("Test-" + i));
        }
    }

    @Override
    public List<Project> getRecentProjects() {
        return recentProjects;
    }

    @Override
    public Project getMostRecentProject() {
        return getRecentProjects().get(0);
    }

    @Override
    public void setMostRecentProject(Project project) {

    }

    @Override
    public void save() {
        System.out.println("SAVED");
    }
}
