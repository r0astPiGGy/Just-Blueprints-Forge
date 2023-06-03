package com.rodev.jbpcore.ui.fragment;

import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.util.DataSet;
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

    @Nullable
    @Override
    public View onCreateView(@Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
        var root = new FrameLayout();

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
