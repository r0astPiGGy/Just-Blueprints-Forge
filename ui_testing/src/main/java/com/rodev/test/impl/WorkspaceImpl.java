package com.rodev.test.impl;

import com.rodev.test.workspace.ProgramData;
import com.rodev.test.workspace.Project;
import com.rodev.test.workspace.Workspace;
import com.rodev.test.workspace.impl.ProjectImpl;

import java.io.File;

public class WorkspaceImpl implements Workspace {

    private final ProgramData programData;
    private final File programDirectory = new File("justblueprints");
    private final File projectDirectory;

    public WorkspaceImpl() {
        projectDirectory = new File(programDirectory, "projects");
        projectDirectory.mkdirs();

        programData = new ProgramDataImpl(this);
    }

    @Override
    public File getProgramDirectory() {
        return programDirectory;
    }

    @Override
    public File getProjectsDirectory() {
        return projectDirectory;
    }

    @Override
    public ProgramData getProgramData() {
        return programData;
    }

    @Override
    public Project createProject(String name) {
        var directory = new File(getProjectsDirectory(), name);

        directory.mkdirs();

        return new ProjectImpl(name, directory);
    }
}
