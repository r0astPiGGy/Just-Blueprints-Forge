package com.rodev.jmcgenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodev.jmcgenerator.data.GeneratorData;
import com.rodev.jmcgenerator.entity.GeneratorEntity;

import java.io.File;
import java.io.IOException;

public class Generator {
    public static void main(String[] args) throws IOException {
        var generatorData = new GeneratorData();

        var generatorDataFile = parserDirectoryChild("generator_data.json");
        var objectMapper = new ObjectMapper();

        var data = objectMapper.readValue(generatorDataFile, GeneratorEntity[].class);
        generatorData.load(data);

        var generator = new CodeGenerator(
                generatorOutputChild("data.jbp"),
                generatorOutputChild("generated.jc")
        );

        generator.generate(generatorData, 4);
    }

    public static File directoryChild(String dir, String fileName) {
        File file = new File(dir);
        //noinspection ResultOfMethodCallIgnored
        file.mkdir();

        return new File(file, fileName);
    }

    public static File generatorOutputChild(String fileName) {
        return directoryChild("generator-output", fileName);
    }

    public static File parserDirectoryChild(String fileName) {
        return directoryChild("parser-output", fileName);
    }
}