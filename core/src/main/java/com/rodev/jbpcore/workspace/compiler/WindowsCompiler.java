package com.rodev.jbpcore.workspace.compiler;

import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class WindowsCompiler extends CodeCompiler {

    public WindowsCompiler(File compilerPath) {
        super(compilerPath);
    }

    @Override
    public File compile(File fileToCompile) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(compilerPath.getParentFile());

        var cmd = getCompileMode().getCommand(compilerPath.getName(), fileToCompile.getAbsolutePath());

        processBuilder.command("cmd.exe", "/c", cmd);

        Process process = processBuilder.start();

        StringBuilder output = new StringBuilder();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        int exitVal = process.waitFor();
        setOutput(output.toString(), exitVal);

        return new File(fileToCompile.getParent(), fileToCompile.getName().split("\\.")[0] + ".json");
    }

}
