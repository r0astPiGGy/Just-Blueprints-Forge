package com.rodev.jbp.client;

import com.rodev.jbpcore.fragment.editor.EditorEventListener;
import com.rodev.jbpcore.workspace.Project;
import com.rodev.jbpcore.workspace.compiler.CodeCompiler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

// TODO
public class ForgeEditorEventListener implements EditorEventListener {
    @Override
    public void onProjectCompileButtonClicked(Project project) {

    }

    @Override
    public void onProjectCompiled(Project project, String message, CodeCompiler.CompileMode compileMode) {
        switch (compileMode) {
            case COMPILE_TO_FILE -> sendProjectCompiledToFileMessage(project, message);
            case COMPILE_AND_UPLOAD -> sendProjectCompiledToCloudMessage(project, message);
        }
    }

    private void sendProjectCompiledToFileMessage(Project project, String message) {
        postSendMessage(message);
    }

    private void sendProjectCompiledToCloudMessage(Project project, String message) {
        postSendMessage(message);
    }

    @Override
    public void onProjectCompileError(Project project, String message) {
        postSendMessage(ChatFormatting.RED + message);
    }

    public void postSendMessage(String message) {
        Minecraft.getInstance().execute(() -> {
            sendMessage(message);
        });
    }

    public void sendMessage(String message) {
        Minecraft.getInstance().player.sendSystemMessage(Component.literal(message));
    }

    @Override
    public void onProjectSaveButtonClicked(Project project) {

    }

    @Override
    public void onProjectSaved(Project project) {

    }
}
