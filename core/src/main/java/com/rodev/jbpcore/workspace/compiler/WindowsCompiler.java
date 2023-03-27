package com.rodev.jbpcore.workspace.compiler;

import java.io.File;

public class WindowsCompiler extends CodeCompiler {

    public WindowsCompiler(File compilerPath) {
        super(compilerPath);
    }

    @Override
    protected void compileBlueprint(File fileToCompile) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(compilerPath.getParentFile());

        var cmd = getCompileMode().getCommand(compilerPath.getName(), fileToCompile.getAbsolutePath());

        processBuilder.command("cmd.exe", "/c", cmd);

        Process process = processBuilder.start();

        var output = readInputStream(process.getInputStream());

        int exitVal = process.waitFor();
        setOutput(output, exitVal);
    }
}
