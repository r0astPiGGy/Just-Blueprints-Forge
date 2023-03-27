package com.rodev.jbpcore.fragment.editor;

import com.rodev.jbpcore.JustBlueprints;
import com.rodev.jbpcore.blueprint.graph.GraphLayout;
import com.rodev.jbpcore.blueprint.node.BPNode;
import com.rodev.jbpcore.utils.ConsumerListener;
import com.rodev.jbpcore.utils.ListenerWrapper;
import com.rodev.jbpcore.view.MaterialButton;
import com.rodev.jbpcore.workspace.Project;

import java.util.LinkedList;
import java.util.function.Consumer;

public class EditorController {

    private static final String LISTENER_NAMESPACE = "editor_listener";

    public void onCompileButtonInit(MaterialButton compileButton) {
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
        var nodes = new LinkedList<BPNode>();

        for(int i = 0; i < graphLayout.getChildCount(); i++) {
            var view = graphLayout.getChildAt(i);

            if(!(view instanceof BPNode node)) continue;

            nodes.add(node);
        }

        project.getBlueprint().save(nodes);
        JustBlueprints.getEditorEventListener().onProjectSaved(project);
    }

    public void onCompileButtonClicked(Project project, GraphLayout graphLayout) {
        JustBlueprints.getEditorEventListener().onProjectCompileButtonClicked(project);
        saveProject(project, graphLayout);
        project.getBlueprint().compile();
    }

}
