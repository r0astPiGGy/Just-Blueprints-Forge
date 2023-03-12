package com.rodev.jbpcore.contextmenu;

import com.rodev.jbpcore.utils.TextViewCreationListener;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.RelativeLayout;
import icyllis.modernui.widget.ScrollView;
import icyllis.modernui.widget.TextView;
import org.jetbrains.annotations.NotNull;

public class ContextMenuRootView extends RelativeLayout {
    private final int popupHeaderId = 45145;
    private final int popupSearchBarId = 454551;

    private final ScrollView scrollView;
    private final TextView headerTextView;
    private final PopupSearchView searchView;

    public ContextMenuRootView() {
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        setGravity(Gravity.TOP | Gravity.CENTER);

        searchView = createSearchView();
        headerTextView = createHeaderLabel();
        scrollView = createContextMenuContent();

        addView(searchView);
        addView(headerTextView);
        addView(scrollView);
    }

    private TextView createHeaderLabel() {
        var headerLabel = new TextView();

        headerLabel.setId(popupHeaderId);
        TextViewCreationListener.onContextMenuHeaderTextCreated(headerLabel);

        var params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.addRule(RelativeLayout.ALIGN_PARENT_START);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        headerLabel.setLayoutParams(params);

        return headerLabel;
    }

    private PopupSearchView createSearchView() {
        var searchView = new PopupSearchView();
        searchView.setId(popupSearchBarId);

        var params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        params.addRule(RelativeLayout.BELOW, popupHeaderId);
        params.setMargins(0, 5, 0, 5);

        searchView.setLayoutParams(params);

        return searchView;
    }

    private ScrollView createContextMenuContent() {
        var scrollView = new ScrollView();
        var params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        params.addRule(RelativeLayout.BELOW, popupSearchBarId);

        scrollView.setLayoutParams(params);

        return scrollView;
    }

    public void setHeader(String header) {
        headerTextView.setText(header);
    }

    public ScrollView getScrollView() {
        return scrollView;
    }

    public PopupSearchView getSearchView() {
        return searchView;
    }

    @Override
    public boolean onTouchEvent(@NotNull MotionEvent event) {
        return true;
    }

}
