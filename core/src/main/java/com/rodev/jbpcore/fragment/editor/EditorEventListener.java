package com.rodev.jbpcore.fragment.editor;

import com.rodev.jbpcore.workspace.Project;

public interface EditorEventListener {

    default void onProjectCompileButtonClicked(Project project) {

    }

    default void onProjectCompiled(Project project, String message) {

    }

    default void onProjectCompileError(Project project, String message) {

    }

    default void onProjectSaveButtonClicked(Project project) {

    }

    default void onProjectSaved(Project project) {

    }

}
