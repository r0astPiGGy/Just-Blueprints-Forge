package com.rodev.test.fragment.editor;

import com.rodev.test.blueprint.graph.GraphLayout;
import com.rodev.test.blueprint.node.BPNode;
import com.rodev.test.workspace.Project;

import java.util.LinkedList;
import java.util.List;

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
