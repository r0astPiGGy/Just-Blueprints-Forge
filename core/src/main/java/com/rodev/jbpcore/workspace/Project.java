package com.rodev.jbpcore.workspace;

import com.rodev.jbpcore.blueprint.Blueprint;

import java.io.File;

public interface Project {

    String getName();

    File getDirectory();

    Blueprint getBlueprint();

    long getCreatedDate();

    long getLastOpenDate();

    void setLastOpenDate(long date);

    void saveInfo();

}
