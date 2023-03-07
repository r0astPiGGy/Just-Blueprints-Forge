package com.rodev.test.workspace;

import java.io.File;

public interface Workspace {

    String BLUEPRINTS_EXTENSION = "jbp";

    File getProgramDirectory();

    File getProjectsDirectory();

    ProgramData getProgramData();

    Project createProject(String name);

}
