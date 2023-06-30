package com.rodev.jbpcore.ui.fragment;

import com.rodev.jbpcore.workspace.ModernUIWindowManager;
import icyllis.modernui.fragment.Fragment;
import lombok.Setter;

@Setter
public class LifecycleFragment extends Fragment {

    @Override
    public void onDestroy() {
        super.onDestroy();

        log("ON DESTROY");
    }

    @Override
    public void onStop() {
        super.onStop();

        log("ON STOP");
    }

    @Override
    public void onStart() {
        super.onStart();

        log("ON START");
    }

    @Override
    public void onDetach() {
        super.onDetach();

        log("ON DETACH");
    }

    @Override
    public void onPause() {
        super.onPause();

        log("ON PAUSE");
    }

    @Override
    public void onResume() {
        super.onResume();

        log("ON RESUME");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        log("ON DESTROY VIEW");
    }

    public void onLateCloseFromWindowManager(ModernUIWindowManager windowManager) {}

    private void log(String msg) {
        System.out.println("[" + this + "] " + msg);
    }
}
