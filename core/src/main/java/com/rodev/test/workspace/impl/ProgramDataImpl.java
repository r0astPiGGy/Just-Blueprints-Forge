package com.rodev.test.workspace.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodev.test.workspace.ProgramData;
import com.rodev.test.workspace.Project;
import com.rodev.test.workspace.Workspace;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ProgramDataImpl implements ProgramData {
    private final List<Project> recentProjects = new LinkedList<>();

    private Set<String> recentProjectNames = new HashSet<>();

    private final WorkspaceImpl workspace;

    private static final String cacheFileName = ".cache";

    public ProgramDataImpl(WorkspaceImpl workspace) {
        this.workspace = workspace;
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
    public void addRecentProject(String name) {
        recentProjectNames.add(name);
    }

    @Override
    public void removeRecentProject(String name) {
        recentProjectNames.remove(name);

        recentProjects.stream()
                .filter(project -> project.getName().equals(name))
                .findAny()
                .ifPresent(recentProjects::remove);
    }

    @Override
    public void reload(Workspace workspace) {
        recentProjects.clear();
        recentProjectNames.clear();

        try {
            loadFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        var forRemoval = new HashSet<String>();

        for (String recentProjectName : recentProjectNames) {
            var project = workspace.getByName(recentProjectName);

            if(project == null) {
                forRemoval.add(recentProjectName);
                continue;
            }

            recentProjects.add(project);
        }

        forRemoval.forEach(this::removeRecentProject);
        save();
    }

    private void loadFromFile() throws IOException {
        var file = new File(workspace.getProgramDirectory(), cacheFileName);

        var objectMapper = new ObjectMapper();

        var data = objectMapper.readValue(file, ProgramDataEntity.class);

        recentProjectNames = new HashSet<>(data.recentProjects);
    }

    @Override
    public void save() {
        var file = new File(workspace.getProgramDirectory(), cacheFileName);

        var objectMapper = new ObjectMapper();

        try {
            objectMapper.writeValue(file, new ProgramDataEntity(recentProjectNames));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    private static class ProgramDataEntity {
        public Set<String> recentProjects;
    }
}
