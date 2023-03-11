package com.rodev.test;

import com.rodev.test.workspace.WindowManager;
import icyllis.modernui.ModernUI;
import icyllis.modernui.fragment.Fragment;

public class WindowManagerImpl extends WindowManager {

    private final ModernUI app;

    public WindowManagerImpl(ModernUI app) {
        this.app = app;
    }

    @Override
    protected void openFragment(Fragment fragment) {
        app.run(fragment);
    }
}
