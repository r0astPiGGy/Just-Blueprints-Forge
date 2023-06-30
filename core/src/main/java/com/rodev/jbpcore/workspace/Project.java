package com.rodev.jbpcore.workspace;

import com.rodev.jbpcore.blueprint.BlueprintReference;

import java.io.File;

public interface Project {

    String getName();

    File getDirectory();

    long getCreatedDate();

    long getLastOpenDate();

    void setLastOpenDate(long date);

    BlueprintReference getBlueprint();

    void saveInfo();

}
