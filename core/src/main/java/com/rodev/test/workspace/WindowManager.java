package com.rodev.test.workspace;

import com.rodev.test.fragment.editor.EditorFragment;
import com.rodev.test.fragment.welcome.WelcomeScreenFragment;
import icyllis.modernui.fragment.Fragment;
import lombok.Setter;

public abstract class WindowManager {

    public static final WelcomeScreenState WELCOME_SCREEN = new WelcomeScreenState();
    public static final EditorScreenState EDITOR_SCREEN = new EditorScreenState();
    private State state = WELCOME_SCREEN;

    private Fragment currentFragment;

    public void open() {
        state.onOpen(this);
        var fragment = state.getFragment();
        if(currentFragment == null) {
            currentFragment = fragment;
            openFragment(fragment);
        } else {
            currentFragment.getParentFragmentManager().beginTransaction()
                    .replace(currentFragment.getId(), fragment)
                    .disallowAddToBackStack()
                    .commit();
            currentFragment = fragment;
        }
    }

    public void close() {
        state.onClose(this);
    }

    public void setState(State state) {
        this.state = state;
    }

    abstract
    protected void openFragment(Fragment fragment);

    public abstract static class State {

        protected State() {}

        protected void onOpen(WindowManager windowManager) {}

        protected void onClose(WindowManager windowManager) {}

        abstract
        protected Fragment getFragment();
    }

    public static class WelcomeScreenState extends State {
        @Override
        protected Fragment getFragment() {
            return new WelcomeScreenFragment();
        }
    }

    public static class EditorScreenState extends State {

        @Setter
        private Project projectToView;

        @Override
        protected Fragment getFragment() {
            return new EditorFragment(projectToView);
        }
    }
}
