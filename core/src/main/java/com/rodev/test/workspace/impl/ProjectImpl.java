package com.rodev.test.workspace.impl;

import com.rodev.test.blueprint.node.BPNode;
import com.rodev.test.workspace.Blueprint;
import com.rodev.test.workspace.Project;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.util.Collection;

@Getter
@RequiredArgsConstructor
public abstract class ProjectImpl implements Project {

    private final String name;
    private final File directory;
    private final long createdDate;
    @Setter
    private transient long lastOpenDate;

    @Override
    public Blueprint getBlueprint() {
        return this::onBlueprintSave;
    }

    abstract
    protected void onBlueprintSave(Collection<BPNode> nodes);
}
