package com.rodev.test.blueprint.data;

import com.rodev.test.blueprint.data.action.Action;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class IconPathResolver {

    private static final Map<String, Resolver> iconPathResolvers = new HashMap<>();

    public static void registerResolver(String namespace, Resolver resolver) {
        iconPathResolvers.put(namespace, resolver);
    }

    public static String resolve(Action action) {
        return iconPathResolvers
                .getOrDefault(action.iconNamespace(), Resolver.identity)
                .resolve(action);
    }

    public interface Resolver {

        Resolver identity = action -> String.format("%s%s%s.png", action.iconNamespace(), File.separator, action.id());

        String resolve(Action action);

    }

}
