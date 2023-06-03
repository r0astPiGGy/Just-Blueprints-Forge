package com.rodev.jbpcore.data;

import java.io.InputStream;

public interface DataProvider {

    InputStream getActionsInputStream();

    InputStream getCategoriesInputStream();

    InputStream getVariableTypesInputStream();

    InputStream getActionTypesInputStream();

    InputStream getSelectorGroupsInputStream();

    InputStream getDataGeneratorInputStream();

    InputStream getPinIconsInputStream();

}
