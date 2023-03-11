package com.rodev.test.workspace.impl;

import com.rodev.test.workspace.Project;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.File;

@Getter
@RequiredArgsConstructor
public abstract class ProjectImpl implements Project {

    private final String name;
    private final File directory;
    private final long createdDate;
    @Setter
    private transient long lastOpenDate;

    @Override
    public Object loadBlueprint() {
        return null;
    }
}
