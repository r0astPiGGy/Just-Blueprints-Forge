package com.rodev.test.workspace;

import java.io.File;

public interface Project {

    String getName();

    File getDirectory();

    Object loadBlueprint();

    void saveBlueprint(Object object);

}
