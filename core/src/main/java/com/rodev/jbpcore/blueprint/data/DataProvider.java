package com.rodev.jbpcore.blueprint.data;

import java.io.InputStream;

public interface DataProvider {

    InputStream getActionsInputStream();

    InputStream getCategoriesInputStream();

    InputStream getVariableTypesInputStream();

    InputStream getActionTypesInputStream();

    InputStream getSelectorGroupsInputStream();

    InputStream getDataGeneratorInputStream();

}
