package com.rodev.jbpcore.workspace.compiler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.*;
import java.util.function.Consumer;

public abstract class CodeCompiler {

    private static final OperatingSystem os = OperatingSystem.getOperatingSystem();

    protected final File compilerPath;

    @Setter
    @Getter
    protected CompileMode compileMode = CompileMode.COMPILE_TO_FILE;

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

    protected String readInputStream(InputStream is) throws Exception {
        StringBuilder output = new StringBuilder();

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        return output.toString();
    }

    abstract
    protected void compileBlueprint(File file) throws Exception;

    public File compile(File fileToCompile) {
        try {
            if(!compilerPath.exists())
                throw new IllegalArgumentException("Provided compiler doesn't exist: " + compilerPath.getAbsolutePath());

            compileBlueprint(fileToCompile);
        } catch (Exception e) {
            var stacktrace = new StringWriter();
            e.printStackTrace(new PrintWriter(stacktrace));
            setOutput(stacktrace.toString(), -1);
        }

        return getCompiledFilePath(fileToCompile);
    }

    private File getCompiledFilePath(File fileToCompile) {
        var parent = fileToCompile.getParent();
        var fileNameAndExtension = fileToCompile.getName().split("\\.");
        var fileName = fileNameAndExtension[0];

        var targetFileName = fileName + ".json";

        return new File(parent, targetFileName);
    }

    public static CodeCompiler getCompiler(File compilerPath) {
        return os.getCompiler(compilerPath);
    }

    public static File getCompilerFile(File directory) {
        return new File(directory, os.compilerFileName);
    }

    @RequiredArgsConstructor
    public enum CompileMode {
        COMPILE_TO_FILE("%s compile %s"),
        COMPILE_AND_UPLOAD("%s compile -u %s")

        ;

        private final String command;

        public String getCommand(String compilerPath, String filePath) {
            return String.format(command, compilerPath, filePath);
        }
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
