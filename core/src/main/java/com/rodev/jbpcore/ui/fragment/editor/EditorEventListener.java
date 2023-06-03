package com.rodev.jbpcore.ui.fragment.editor;

import com.rodev.jbpcore.workspace.Project;
import com.rodev.jbpcore.workspace.compiler.CodeCompiler;

public interface EditorEventListener {

    default void onProjectCompileButtonClicked(Project project) {

    }

    default void onProjectCompiled(Project project, String message, CodeCompiler.CompileMode compileMode) {

    }

    default void onProjectCompileError(Project project, String message) {

    }

    default void onProjectSaveButtonClicked(Project project) {

    }

    default void onProjectSaved(Project project) {

    }

}
