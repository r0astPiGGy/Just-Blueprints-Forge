package com.rodev.test;

import icyllis.modernui.ModernUI;
import icyllis.modernui.test.TestFragment;

public class Main {
    public static void main(String[] args) {

        try (ModernUI app = new ModernUI()) {
            app.run(new BlueprintFragment());
        }
    }
}