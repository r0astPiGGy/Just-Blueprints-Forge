package com.rodev.jbpcore.workspace;

import com.rodev.jbpcore.fragment.ContainerFragment;
import com.rodev.jbpcore.fragment.LifecycleFragment;
import com.rodev.jbpcore.fragment.editor.EditorFragment;
import com.rodev.jbpcore.fragment.welcome.WelcomeScreenFragment;
import icyllis.modernui.fragment.Fragment;
import lombok.Setter;

import java.lang.ref.WeakReference;

public abstract class WindowManager {

    public static final WelcomeScreenState WELCOME_SCREEN = new WelcomeScreenState();
    public static final EditorScreenState EDITOR_SCREEN = new EditorScreenState();
    protected State state = WELCOME_SCREEN;

    public void open() {
        var fragment = state.getFragment();
        openFragment(new ContainerFragment(fragment));
    }

    public void transactFrom(Fragment fragment) {
        var destination = state.getFragment();

        var fragmentManager = fragment.getParentFragmentManager();

        fragmentManager.beginTransaction()
                .replace(fragment.getId(), destination)
                .commit();
    }

    public void setState(State state) {
        this.state = state;
    }

    abstract
    protected void openFragment(Fragment fragment);

    public abstract static class State {

        protected State() {}

        abstract
        protected LifecycleFragment getFragment();
    }

    public static class WelcomeScreenState extends State {
        @Override
        protected LifecycleFragment getFragment() {
            return new WelcomeScreenFragment();
        }
    }

    public static class EditorScreenState extends State {

        @Setter
        private Project projectToView;

        @Override
        protected LifecycleFragment getFragment() {
            return new EditorFragment(projectToView);
        }
    }
}
