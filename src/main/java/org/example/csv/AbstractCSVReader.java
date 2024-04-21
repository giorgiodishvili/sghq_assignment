package org.example.csv;


import org.example.logger.AppLogger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCSVReader<T> {
    private final String filePath;

    public AbstractCSVReader(String filePath) {
        this.filePath = filePath;
    }

    public final List<T> readCSV() {
        AppLogger.logInfo("Started reading from file: %s".formatted(filePath));
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return reader.lines()
                    .skip(1)
                    .map(this::processLine)
                    .toList();
        } catch (UncheckedIOException | IOException exception) {
            AppLogger.logError("Exception while reading CSV file path: %s \n"
                    .formatted(filePath), exception);
        }
        return Collections.emptyList();
    }

    protected abstract T processLine(String line);
}
