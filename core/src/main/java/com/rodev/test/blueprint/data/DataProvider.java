package com.rodev.test.blueprint.data;

import java.io.InputStream;

public interface DataProvider {

    InputStream getActionsInputStream();

    InputStream getCategoriesInputStream();

    InputStream getVariableTypesInputStream();

    InputStream getActionTypesInputStream();

    InputStream getSelectorGroupsInputStream();

}
