package com.rodev.test.workspace;

import java.util.List;

public interface ProgramData {

    List<Project> getRecentProjects();

    Project getMostRecentProject();

    void setMostRecentProject(Project project);

    void addRecentProject(String name);

    void removeRecentProject(String name);

    void reload(Workspace workspace);

    void save();

}
