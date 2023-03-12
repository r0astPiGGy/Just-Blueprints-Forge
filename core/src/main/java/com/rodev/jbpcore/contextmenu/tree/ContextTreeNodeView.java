package com.rodev.jbpcore.contextmenu.tree;

import com.rodev.jbpcore.contextmenu.Item;
import icyllis.modernui.view.View;
import icyllis.modernui.widget.LinearLayout;

import java.util.*;
import java.util.function.Predicate;

public class ContextTreeNodeView extends ContextTreeRootView {

    private final TreeNodeHeaderLabel header;
    private final LinearLayout content = new LinearLayout();
    private final Set<Item> itemList = new HashSet<>();

    private boolean opened = true;

    private String name;

    public ContextTreeNodeView(String name) {
        this.name = name;

        setOrientation(VERTICAL);

        header = new TreeNodeHeaderLabel(name, leftPadding -> {
            content.setPadding(leftPadding, 0, 0, 0);
        });
        header.setOnClickListener(view -> {
            toggle();
        });
        content.setOrientation(VERTICAL);
        close();

        addView(header, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        addView(content, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    public void setName(String name) {
        this.name = name;
        header.setText(name);
    }

    public String getName() {
        return name;
    }

    public void add(Item item) {
        itemList.add(item);
        content.addView(item.getView(), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void onAddView(ContextTreeNodeView node) {
        content.addView(node, 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    public void toggle() {
        if(opened) {
            close();
        } else {
            open();
        }
    }

    public void open() {
        header.setArrowEnabled(true);
        content.setVisibility(View.VISIBLE);
        opened = true;
    }

    public void close() {
        header.setArrowEnabled(false);
        content.setVisibility(View.GONE);
        opened = false;
    }

    public boolean hideIfNot(Predicate<Item> contextMenuItemPredicate){
        var found = super.hideIfNot(contextMenuItemPredicate);

        for(Item item : itemList) {
            var tested = contextMenuItemPredicate.test(item);
            if(!tested) {
                item.hide();
            } else {
                item.show();
            }
            if(tested && !found) {
                found = true;
            }
        }

        if(found) {
            open();
            show();
        } else {
            hide();
        }

        return found;
    }

    public void hide() {
        if(getVisibility() == View.VISIBLE) {
            setVisibility(View.GONE);
        }
    }

    public void show() {
        if(getVisibility() == View.GONE) {
            setVisibility(View.VISIBLE);
        }
    }

}
