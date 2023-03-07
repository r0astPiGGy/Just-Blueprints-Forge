package com.rodev.test.workspace.impl;

import com.rodev.test.workspace.Project;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;

@Getter
@RequiredArgsConstructor
public class ProjectImpl implements Project {

    private final String name;
    private final File directory;

    @Override
    public Object loadBlueprint() {
        return null;
    }

    @Override
    public void saveBlueprint(Object object) {

    }
}
