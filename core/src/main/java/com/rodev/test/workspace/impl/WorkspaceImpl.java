package com.rodev.test.workspace.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodev.test.fragment.welcome.ValidateResult;
import com.rodev.test.workspace.ProgramData;
import com.rodev.test.workspace.Project;
import com.rodev.test.workspace.Workspace;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class WorkspaceImpl implements Workspace {

    private final ProgramData programData;
    private final File programDirectory = new File("justblueprints");
    private final File projectDirectory;
    private static final Pattern projectNamePattern = Pattern.compile("^[a-zA-Z\\d\\-]{1,30}$");
    private static final String projectInfoFileName = "project.json";

    private final Map<String, Project> loadedProjects = new HashMap<>();

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

    // TODO async
    @Override
    public void reloadProjects() {
        loadedProjects.clear();
        for (File directory : getProjectsDirectory().listFiles()) {
            var project = loadFromDirectory(directory);

            if(project == null) continue;

            loadedProjects.put(project.getName(), project);
        }

        programData.reload(this);
    }

    public Project createProject(File directory, ProjectInfoEntity infoEntity) {
        return new ProjectImpl(infoEntity.name, directory, infoEntity.created) {
            @Override
            public void saveBlueprint(Object object) {

            }

            @Override
            public void saveInfo() {
                try {
                    writeProjectInfo(this);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @Override
    public Project createProject(String name) {
        var directory = new File(getProjectsDirectory(), name);

        if(projectExists(name))
            throw new IllegalStateException("Project by name " + name + " already exists");

        directory.mkdirs();

        var project = new ProjectImpl(name, directory, new Date().getTime()) {
            @Override
            public void saveBlueprint(Object object) {

            }

            @Override
            public void saveInfo() {
                try {
                    writeProjectInfo(this);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        project.saveInfo();

        loadedProjects.put(name, project);

        return project;
    }

    @Override
    public @Nullable Project getByName(String name) {
        return loadedProjects.get(name);
    }

    public boolean projectExists(String name) {
        return loadedProjects.containsKey(name);
    }

    @Nullable
    public Project loadFromDirectory(File directory) {
        var projectInfoFile = new File(directory, projectInfoFileName);

        if(!projectInfoFile.exists()) {
            return null;
        }

        var objectMapper = new ObjectMapper();

        ProjectInfoEntity data;

        try {
            data = objectMapper.readValue(projectInfoFile, ProjectInfoEntity.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return createProject(directory, data);
    }

    private void writeProjectInfo(Project project) throws IOException {
        var file = new File(project.getDirectory(), projectInfoFileName);

        var objectMapper = new ObjectMapper();
        objectMapper.writeValue(file, ProjectInfoEntity.from(project));
    }

    @AllArgsConstructor
    @NoArgsConstructor
    private static class ProjectInfoEntity {
        public String name;
        public long lastOpen;
        public long created;

        @JsonIgnore
        public static ProjectInfoEntity from(Project project) {
            return new ProjectInfoEntity(
                    project.getName(),
                    project.getLastOpenDate(),
                    project.getCreatedDate()
            );
        }
    }

    @Override
    public void validateProjectName(String name, ValidateResult validateResult) {
        boolean matches = projectNamePattern.matcher(name).matches();

        if(!matches) {
            validateResult.asError("Ввод не соответствует выражению " + projectNamePattern.pattern());
            return;
        }

        if(projectExists(name)) {
            validateResult.asError("Проект с этим именем уже существует");
        }
    }
}
