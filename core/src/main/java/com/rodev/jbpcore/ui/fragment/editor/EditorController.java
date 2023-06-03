package com.rodev.jbpcore.ui.fragment.editor;

import com.rodev.jbpcore.JustBlueprints;
import com.rodev.jbpcore.blueprint.graph.GraphLayout;
import com.rodev.jbpcore.blueprint.node.GraphNode;
import com.rodev.jbpcore.workspace.Project;
import com.rodev.jbpcore.workspace.compiler.CodeCompiler;

import java.util.LinkedList;

public class EditorController {

    private static final String LISTENER_NAMESPACE = "editor_listener";

    public void onCompileButtonInit(CompileButton compileButton) {
        var workspace = JustBlueprints.getWorkspace();
        var compiler = workspace.getCompiler();

        if(compiler.isWorking()) {
            compileButton.setEnabled(false);
        }

        compiler.getStateChangeListeners()
                .addListener(compileButton::setEnabled)
                .setNamespace(LISTENER_NAMESPACE);
    }

    public void onDestroy() {
        JustBlueprints.getWorkspace()
                .getCompiler()
                .getStateChangeListeners()
                .removeAllByNamespace(LISTENER_NAMESPACE);
    }

    public void onSaveButtonClicked(Project project, GraphLayout graphLayout) {
        JustBlueprints.getEditorEventListener().onProjectSaveButtonClicked(project);
        saveProject(project, graphLayout);
    }

    public void saveProject(Project project, GraphLayout graphLayout) {
        var nodes = new LinkedList<GraphNode>();

        for(int i = 0; i < graphLayout.getChildCount(); i++) {
            var view = graphLayout.getChildAt(i);

            if(!(view instanceof GraphNode node)) continue;

            nodes.add(node);
        }

        project.getBlueprint().save(nodes);
        JustBlueprints.getEditorEventListener().onProjectSaved(project);
    }

    public void onCompileButtonClicked(Project project, CodeCompiler.CompileMode compileMode) {
        JustBlueprints.getEditorEventListener().onProjectCompileButtonClicked(project);
        project.getBlueprint().compile(compileMode);
    }

}
