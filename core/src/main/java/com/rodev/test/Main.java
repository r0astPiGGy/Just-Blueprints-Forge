package com.rodev.test;

import com.rodev.test.blueprint.data.DataAccess;
import icyllis.modernui.ModernUI;
import icyllis.modernui.test.TestFragment;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        DataAccess.load(Main.class.getResourceAsStream("data.json"));

        try (ModernUI app = new ModernUI()) {
            app.run(new BlueprintFragment());
        }
    }
}