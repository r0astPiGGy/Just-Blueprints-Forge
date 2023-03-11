package com.rodev.test.workspace;

import com.rodev.test.fragment.welcome.ValidateResult;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public interface Workspace {

    String BLUEPRINTS_EXTENSION = "jbp";

    File getProgramDirectory();

    File getProjectsDirectory();

    ProgramData getProgramData();

    void reloadProjects();

    Project createProject(String name);

    @Nullable
    Project getByName(String name);

    void validateProjectName(String name, ValidateResult validateResult);

}
