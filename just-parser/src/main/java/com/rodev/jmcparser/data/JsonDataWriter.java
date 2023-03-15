package com.rodev.jmcparser.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@RequiredArgsConstructor
public class JsonDataWriter {

    private final File fileToWriteTo;

    private File[] files;

    public void write(Object objectToWrite) {
        var objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        try {
            objectMapper.writeValue(fileToWriteTo, objectToWrite);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(files == null) return;

        for (File file : files) {
            try {
                if(!file.isDirectory()) {
                    throw new IllegalStateException("File " + file + " is not directory!");
                }
                Files.copy(fileToWriteTo.toPath(), new File(file, fileToWriteTo.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setAdditionalDestinationDirectories(File[] files) {
        this.files = files;
    }
}
