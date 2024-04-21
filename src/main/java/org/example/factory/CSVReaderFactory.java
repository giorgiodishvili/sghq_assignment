package org.example.factory;

import org.example.csv.AbstractCSVReader;

public interface CSVReaderFactory {
    <T> AbstractCSVReader<T> createCSVReader(Class<T> tClass, String filePath);
}
