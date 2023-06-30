package com.rodev.jbp.client;

import com.rodev.jbpcore.blueprint.BlueprintReference;
import com.rodev.jbpcore.ui.fragment.editor.EditorEventListener;
import com.rodev.jbpcore.workspace.Project;
import com.rodev.jbpcore.workspace.compiler.CodeCompiler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.*;

import java.util.regex.Pattern;

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
        var regex = "/module.+\n";

        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(message);

        String tempCommand = "null";

        if(matcher.find()) {
            tempCommand = matcher.group();
        }

        var command = tempCommand;

        var split = message.split(regex);

        var msg = Component.literal(split[0])
                .append(Component.literal(command).withStyle(s -> {
                    return s.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Кликни, чтобы ввести команду")))
                            .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command))
                            .withColor(ChatFormatting.GREEN);
                }))
                .append(Component.literal(split[1]));

        postSendMessage(msg);
    }

    @Override
    public void onBlueprintCompileError(BlueprintReference blueprint, String message) {
        postSendMessage(ChatFormatting.RED + message);
    }

    public void postSendMessage(String message) {
        Minecraft.getInstance().execute(() -> {
            sendMessage(message);
        });
    }

    public void postSendMessage(Component component) {
        Minecraft.getInstance().execute(() -> {
            sendMessage(component);
        });
    }

    public void sendMessage(String message) {
        sendMessage(Component.literal(message));
    }

    public void sendMessage(Component component) {
        Minecraft.getInstance().player.sendSystemMessage(component);
    }

    @Override
    public void onProjectSaveButtonClicked(Project project) {

    }

    @Override
    public void onProjectSaved(Project project) {

    }
}
