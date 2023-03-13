package com.rodev.jmcparser.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
public class JsonDataWriter {

    private final File fileToWriteTo;

    public void write(Object objectToWrite) {
        var objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        try {
            objectMapper.writeValue(fileToWriteTo, objectToWrite);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
