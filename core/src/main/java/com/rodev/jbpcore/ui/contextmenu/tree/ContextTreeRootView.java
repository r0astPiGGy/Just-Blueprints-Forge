package com.rodev.jbpcore.ui.contextmenu.tree;

import com.rodev.jbpcore.ui.contextmenu.Item;
import icyllis.modernui.core.Context;
import icyllis.modernui.widget.LinearLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class ContextTreeRootView extends LinearLayout {

    private final Map<String, ContextTreeNodeView> child = new HashMap<>();

    public ContextTreeRootView(Context context) {
        super(context);
        setOrientation(VERTICAL);
    }

    public boolean hideIfNot(Predicate<Item> contextMenuItemPredicate) {
        var found = false;
        for(ContextTreeNodeView treeNode : child.values()) {
            var tested = treeNode.hideIfNot(contextMenuItemPredicate);
            if (tested && !found) {
                found = true;
            }
        }
        return found;
    }

    @Nullable
    public ContextTreeNodeView getChild(String name) {
        return child.get(name);
    }

    public void add(ContextTreeNodeView node) {
        child.put(node.getName(), node);
        onAddView(node);
    }

    public void onAddView(ContextTreeNodeView node) {
        addView(node, 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    @NotNull
    public ContextTreeNodeView getOrCreate(String name) {
        var temp = getChild(name);
        if(temp == null) {
            temp = new ContextTreeNodeView(getContext(), name);
            add(temp);
        }
        return temp;
    }

}
