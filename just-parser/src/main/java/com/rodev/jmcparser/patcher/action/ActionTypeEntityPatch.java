package com.rodev.jmcparser.patcher.action;

import com.rodev.jmcparser.patcher.Patch;
import com.rodev.jmcparser.patcher.Patchable;

public class ActionTypeEntityPatch extends Patch {

    public String id;
    @Patchable
    public String header_color;

    @Override
    public String getPatchedId() {
        return id;
    }
}
