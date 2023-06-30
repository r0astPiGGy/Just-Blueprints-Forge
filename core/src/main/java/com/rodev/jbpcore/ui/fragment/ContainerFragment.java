package com.rodev.jbpcore.ui.fragment;

import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.LayoutInflater;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.FrameLayout;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import static com.rodev.jbpcore.utils.ViewUtils.matchParent;

@RequiredArgsConstructor
public class ContainerFragment extends Fragment {

    public static final int container_id = 514353195;

    public final Fragment childFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, DataSet savedInstanceState) {
        var root = new FrameLayout(getContext());

        matchParent(root);
        root.setId(container_id);

        return root;
    }

    @Override
    public void onCreate(@Nullable DataSet savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getChildFragmentManager().beginTransaction()
                .replace(container_id, childFragment)
                .commit();
    }
}
