package com.rodev.jbpcore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodev.jbpcore.workspace.compiler.CodeCompiler;

import java.io.File;

public class CompilerTest {

    public static void main(String[] args) {
        var generatedCode = generatorOutputChild("generated.jc");

        var jmcc = getJustMCCompiler();

        if(!jmcc.exists()) {
            System.out.println("Code was generated, but jmcc not found. Please install it to "
                    + jmcc.getAbsolutePath());
            return;
        }

        var compiler = CodeCompiler.getCompiler(jmcc);
        File compiled;

        try {
            compiled = compiler.compile(generatedCode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        var output = compiler.getOutput();

        if(compiler.getExitCode() != 0) {
            System.out.println("Compilation finished unsuccessfully. Error message: \n" + output);
            return;
        }

        System.out.println(output);
    }

    private static File getJustMCCompiler() {
        return new File("justblueprints", "jmcc");
    }

    public static File generatorOutputChild(String fileName) {
        return directoryChild("generator-output", fileName);
    }

    public static File parserDirectoryChild(String fileName) {
        return directoryChild("parser-output", fileName);
    }

    public static File directoryChild(String dir, String fileName) {
        File file = new File(dir);
        //noinspection ResultOfMethodCallIgnored
        file.mkdir();

        return new File(file, fileName);
    }

}
