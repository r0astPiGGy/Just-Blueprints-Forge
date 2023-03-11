package com.rodev.test.workspace;

import java.io.File;

public interface Project {

    String getName();

    File getDirectory();

    Object loadBlueprint();

    long getCreatedDate();

    long getLastOpenDate();

    void setLastOpenDate(long date);

    void saveBlueprint(Object object);

    void saveInfo();

}
