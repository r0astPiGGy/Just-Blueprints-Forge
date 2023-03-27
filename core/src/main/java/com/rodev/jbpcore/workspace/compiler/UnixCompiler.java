package com.rodev.jbpcore.workspace.compiler;

import java.io.File;

public class UnixCompiler extends CodeCompiler {

    public UnixCompiler(File compilerPath) {
        super(compilerPath);
    }

    protected void compileBlueprint(File fileToCompile) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(compilerPath.getParentFile());

        var cmd = getCompileMode()
                .getCommand("./" + compilerPath.getName(), fileToCompile.getAbsolutePath());

        processBuilder.command("bash", "-c", cmd);

        Process process = processBuilder.start();

        var output = readInputStream(process.getInputStream());

        int exitVal = process.waitFor();
        setOutput(output, exitVal);
    }
}
