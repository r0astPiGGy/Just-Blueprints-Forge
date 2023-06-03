package com.rodev.jbpcore.workspace.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rodev.jbpcore.JustBlueprints;
import com.rodev.jbpcore.data.DataAccess;
import com.rodev.jbpcore.data.action.Action;
import com.rodev.jbpcore.blueprint.graph.GraphController;
import com.rodev.jbpcore.blueprint.node.GraphNode;
import com.rodev.jbpcore.blueprint.node.PinConnection;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.ui.fragment.welcome.ValidateResult;
import com.rodev.jbpcore.workspace.ProgramData;
import com.rodev.jbpcore.workspace.Project;
import com.rodev.jbpcore.workspace.Workspace;
import com.rodev.jbpcore.workspace.compiler.AsyncCompiler;
import com.rodev.jbpcore.workspace.compiler.CodeCompiler;
import com.rodev.jmcgenerator.CodeGenerator;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@Log4j2
public class WorkspaceImpl implements Workspace {
    private final ProgramData programData;
    private final File programDirectory = new File("justblueprints");
    private final File projectDirectory;
    private static final Pattern projectNamePattern = Pattern.compile("^[a-zA-Z\\d\\-]{1,30}$");
    private static final String projectInfoFileName = "project.json";
    private static final String blueprintDataFileName = "data.jbp";
    private static final String compileDataFileName = "generated.jc";

    private final AsyncCompiler compiler;

    private final Map<String, Project> loadedProjects = new HashMap<>();

    public WorkspaceImpl() {
        projectDirectory = new File(programDirectory, "projects");
        projectDirectory.mkdirs();

        programData = new ProgramDataImpl(this);

        compiler = new AsyncCompiler(programDirectory);
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
    public AsyncCompiler getCompiler() {
        return compiler;
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
        return createProject(infoEntity.name, directory, infoEntity.created, infoEntity.lastOpen);
    }

    private Project createProject(String name, File directory) {
        return createProject(name, directory, new Date().getTime(), new Date().getTime());
    }

    private Project createProject(String name, File directory, long createdDate, long lastOpen) {
        return new ProjectImpl(name, directory, createdDate, lastOpen) {

            @Override
            protected void onBlueprintSave(Collection<GraphNode> nodes) {
                saveBlueprint(getDirectory(), nodes);
            }

            @Override
            protected void onBlueprintLoad(GraphController graphController) {
                loadBlueprint(getDirectory(), graphController);
            }

            @Override
            protected void onBlueprintCompile(CodeCompiler.CompileMode compileMode) {
                compileBlueprint(this, compileMode);
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

    public void compileBlueprint(Project project, final CodeCompiler.CompileMode compileMode) {
        var projectDirectory = project.getDirectory();
        var savedBlueprint = new File(projectDirectory, blueprintDataFileName);
        var generatedCode = new File(projectDirectory, compileDataFileName);

        var codeGenerator = new CodeGenerator(savedBlueprint, generatedCode);

        codeGenerator.generate(DataAccess.getInstance().generatorData, 4);

        var asyncCompiler = getCompiler();

        asyncCompiler.getOnPreCompileListeners().addListener(codeCompiler -> {
            codeCompiler.setCompileMode(compileMode);
        }).setRemoveOnNotify();

        var jmcc = asyncCompiler.getCompilerFile();

        if(!jmcc.exists()) {
            var msg = "Code was generated, but jmcc not found. Please install it to "
                    + jmcc.getAbsolutePath();
            log.error(msg);
            JustBlueprints.getEditorEventListener().onProjectCompileError(project, msg);
            return;
        }

        asyncCompiler.compile(savedBlueprint, codeCompiler -> {
            onBlueprintCompiled(project, codeCompiler);
        });
    }

    private void onBlueprintCompiled(Project project, CodeCompiler compiler) {
        var output = compiler.getOutput();

        if(compiler.getExitCode() != 0) {
            log.warn("Compilation finished unsuccessfully. Error message: \n" + output);
            JustBlueprints.getEditorEventListener().onProjectCompileError(project, output);
            return;
        }

        log.info(output);
        JustBlueprints.getEditorEventListener().onProjectCompiled(project, output, compiler.getCompileMode());
    }


    public void saveBlueprint(File projectDirectory, Collection<GraphNode> nodes) {
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

    public void loadBlueprint(File projectDirectory, GraphController graphController) {
        var file = new File(projectDirectory, blueprintDataFileName);

        var objectMapper = new ObjectMapper();

        BlueprintEntity blueprint;
        try {
            blueprint = objectMapper.readValue(file, BlueprintEntity.class);
        } catch (IOException e) {
            return;
        }

        Map<String, Pin> outputPins = new HashMap<>();
        List<PinConnection> connections = new LinkedList<>();

        for(var nodeEntity : blueprint.nodes) {
            var actionId = nodeEntity.id;
            Action action = DataAccess.getInstance().actionRegistry.get(actionId);

            if(action == null) {
                log.warn("Action by id " + actionId + " not found during blueprint load. (Outdated blueprint?)");
                continue;
            }

            GraphNode node = action.toNode();

            var deserializer = node.getDeserializer(nodeEntity.data);
            deserializer.deserialize();

            outputPins.putAll(deserializer.getOutputPins());
            connections.addAll(deserializer.getPinConnections());

            int x = nodeEntity.position.x;
            int y = nodeEntity.position.y;

            graphController.createNodeAt(x, y, node);
        }

        for(var connection : connections) {
            var outputPinId = connection.outputPin();
            var outputPin = outputPins.get(outputPinId);

            connection.inputPin().connect(outputPin);
        }
    }

    public NodeEntity serialize(GraphNode node) {
        var entity = new NodeEntity();

        entity.id = node.getType();
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
            validateResult.asError("Название имеет некорректный формат. Убедитесь, что нет пробелов, " +
                    "точек и бла бла бла");
            return;
        }

        if(projectExists(name)) {
            validateResult.asError("Проект с этим именем уже существует");
        }
    }
}
