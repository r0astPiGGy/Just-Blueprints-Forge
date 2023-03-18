package com.rodev.jmcgenerator;

import java.util.LinkedList;
import java.util.List;

public class CodeIndentation {

    public static String applyIndentation(String code, int spacing) {
        List<String> lines = new LinkedList<>();

        var linesSplit = code.split("\n");

        var space = 0;

        for(var line : linesSplit) {
            var currentSpace = space;

            if(line.contains("}")) {
                currentSpace -= spacing;
            }

            currentSpace = Math.max(0, currentSpace);

            lines.add(" ".repeat(currentSpace) + line);

            for(var ch : line.toCharArray()) {
                if(ch == '{') {
                    space += spacing;
                }
                if(ch == '}') {
                    space = Math.max(0, space - spacing);
                }
            }
        }

        return String.join("\n", lines);
    }

}
