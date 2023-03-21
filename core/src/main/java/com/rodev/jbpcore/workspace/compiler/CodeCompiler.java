package com.rodev.jbpcore.workspace.compiler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.function.Consumer;

public abstract class CodeCompiler {

    private static final OperatingSystem os = OperatingSystem.getOperatingSystem();

    protected final File compilerPath;

    @Getter
    private String output;
    @Getter
    private int exitCode;

    protected CodeCompiler(File compilerPath) {
        this.compilerPath = compilerPath;
    }

    protected void setOutput(String output, int exitCode) {
        this.output = output;
        this.exitCode = exitCode;
    }

    public abstract File compile(File fileToCompile) throws Exception;

    public static CodeCompiler getCompiler(File compilerPath) {
        return os.getCompiler(compilerPath);
    }

    public static File getCompilerFile(File directory) {
        return new File(directory, os.compilerFileName);
    }

    @RequiredArgsConstructor
    private enum OperatingSystem {
        WINDOWS("jmcc.exe") {
            @Override
            protected CodeCompiler getCompiler(File compilerPath) {
                return new WindowsCompiler(compilerPath);
            }
        },
        UNIX("jmcc") {
            @Override
            protected CodeCompiler getCompiler(File compilerPath) {
                return new UnixCompiler(compilerPath);
            }
        }

        ;

        private final String compilerFileName;

        abstract
        protected CodeCompiler getCompiler(File compilerPath);

        public static OperatingSystem getOperatingSystem() {
            var osName = System.getProperty("os.name").toLowerCase();

            var detected = osName.contains("windows") ? WINDOWS : UNIX;

            System.out.println("Detected operating system: " + detected + ", (" + osName + ")");

            return detected;
        }
    }

}