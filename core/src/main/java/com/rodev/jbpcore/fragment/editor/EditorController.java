package com.rodev.jbpcore.fragment.editor;

import com.rodev.jbpcore.blueprint.graph.GraphLayout;
import com.rodev.jbpcore.blueprint.node.BPNode;
import com.rodev.jbpcore.workspace.Project;

import java.util.LinkedList;

public class EditorController {

    public void onSaveButtonClicked(Project project, GraphLayout graphLayout) {
        var nodes = new LinkedList<BPNode>();

        for(int i = 0; i < graphLayout.getChildCount(); i++) {
            var view = graphLayout.getChildAt(i);

            if(!(view instanceof BPNode node)) continue;

            nodes.add(node);
        }

        long millis = System.currentTimeMillis();
        project.getBlueprint().save(nodes);

        System.out.println("Took " + (System.currentTimeMillis() - millis) + "ms. to save blueprint");
    }

}
