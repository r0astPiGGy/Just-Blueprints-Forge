package com.rodev.jbpcore.ui.fragment;

import com.rodev.jbpcore.workspace.Window;
import icyllis.modernui.fragment.Fragment;

public class WindowFragment extends Fragment implements Window {

    @Override
    public void onOpen() {

    }

    @Override
    public void onTransaction(Window previousDestination) {
        if (!(previousDestination instanceof Fragment fragment)) return;

        var fragmentManager = fragment.getParentFragmentManager();

        fragmentManager.beginTransaction()
                .replace(fragment.getId(), this)
                .commit();
    }
}
