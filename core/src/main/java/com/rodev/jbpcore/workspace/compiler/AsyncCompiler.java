package com.rodev.jbpcore.workspace.compiler;

import com.rodev.jbpcore.utils.ConsumerListener;
import icyllis.modernui.core.Core;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class AsyncCompiler {

    private final File programDirectory;

    private boolean working = false;
    private final Object workingLock = new Object();

    private final ConsumerListener.Notifiable<Boolean> stateChangeListeners = ConsumerListener.create();

    public ConsumerListener<Boolean> getStateChangeListeners() {
        return stateChangeListeners;
    }

    public boolean isCompilerPresent() {
        return getCompilerFile().exists();
    }

    public boolean isWorking() {
        synchronized (workingLock) {
            return working;
        }
    }

    @NotNull
    public File getCompilerFile() {
        return CodeCompiler.getCompilerFile(programDirectory);
    }

    public void compile(File blueprint, final CompilationCallback compiledCallback) {
        var compilerPath = getCompilerFile();
        var compiler = CodeCompiler.getCompiler(compilerPath);

        onPreCompile(compiler);

        setWorking(true);

        CompletableFuture.runAsync(() -> {
            onAsyncCompile(compiler, blueprint, compiledCallback);
        });
    }

    private void onPreCompile(CodeCompiler compiler) {
        compiler.setCompileMode(CodeCompiler.CompileMode.COMPILE_TO_FILE);
    }

    private void onAsyncCompile(CodeCompiler compiler, File blueprint, CompilationCallback callback) {
        compiler.compile(blueprint);

        Core.getUiHandler().post(() -> {
            callback.compiled(compiler);
            setWorking(false);
        });
    }

    private void setWorking(boolean working) {
        synchronized (workingLock) {
            if (this.working == working) return;

            this.working = working;
        }

        stateChangeListeners.notifyListeners(!working);
    }

    @FunctionalInterface
    public interface CompilationCallback {

        void compiled(@NotNull CodeCompiler compiler);

    }

}
