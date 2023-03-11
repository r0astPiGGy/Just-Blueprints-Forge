package com.rodev.test.workspace.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rodev.test.blueprint.node.BPNode;
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
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WorkspaceImpl implements Workspace {

    private final ProgramData programData;
    private final File programDirectory = new File("justblueprints");
    private final File projectDirectory;
    private static final Pattern projectNamePattern = Pattern.compile("^[a-zA-Z\\d\\-]{1,30}$");
    private static final String projectInfoFileName = "project.json";
    private static final String blueprintDataFileName = "data.jbp";

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

    @Override
    public Project createProject(String name) {
        var directory = new File(getProjectsDirectory(), name);

        if(projectExists(name))
            throw new IllegalStateException("Project by name " + name + " already exists");

        directory.mkdirs();

        var project = createProject(name, directory);
        project.saveInfo();

        loadedProjects.put(name, project);

        return project;
    }

    public Project createProject(File directory, ProjectInfoEntity infoEntity) {
        return createProject(infoEntity.name, directory, infoEntity.created);
    }

    private Project createProject(String name, File directory) {
        return createProject(name, directory, new Date().getTime());
    }

    private Project createProject(String name, File directory, long createdDate) {
        return new ProjectImpl(name, directory, createdDate) {

            @Override
            protected void onBlueprintSave(Collection<BPNode> nodes) {
                saveBlueprint(getDirectory(), nodes);
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

    public void saveBlueprint(File projectDirectory, Collection<BPNode> nodes) {
        var file = new File(projectDirectory, blueprintDataFileName);

        var nodeEntities = nodes.stream().map(this::serialize).toList();
        var blueprint = new BlueprintEntity(nodeEntities);

        var objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        try {
            objectMapper.writeValue(file, blueprint);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NodeEntity serialize(BPNode node) {
        var entity = new NodeEntity();

        entity.id = node.getType();
        entity.deserializer = node.getSerializerId();
        entity.position = new NodeLocation(node.getNodeX(), node.getNodeY());
        entity.data = node.serialize();

        return entity;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class BlueprintEntity {
        public List<NodeEntity> nodes;
    }

    public static class NodeEntity {
        public String id;
        public NodeLocation position;
        public String deserializer;
        public Object data;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class NodeLocation {
        public int x, y;
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
