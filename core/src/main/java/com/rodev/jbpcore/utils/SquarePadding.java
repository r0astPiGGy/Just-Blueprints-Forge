package com.rodev.jbpcore.utils;

import icyllis.modernui.view.View;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SquarePadding {

    private final int dp;

    public static SquarePadding of(int dp) {
        return new SquarePadding(dp);
    }

    public void applyTo(View view) {
        view.setPadding(dp, dp, dp, dp);
    }

}
