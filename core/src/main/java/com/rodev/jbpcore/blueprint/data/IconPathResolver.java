package com.rodev.jbpcore.blueprint.data;

import com.rodev.jbpcore.blueprint.data.action.Action;

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

        Resolver identity = action -> String.format("%s/%s.png", action.iconNamespace(), action.id());

        String resolve(Action action);

    }

}
