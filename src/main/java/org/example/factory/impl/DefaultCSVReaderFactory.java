package org.example.factory.impl;

import org.example.dto.EmployeeDto;
import org.example.factory.CSVReaderFactory;
import org.example.factory.enums.CSVTypeRecord;
import org.example.csv.AbstractCSVReader;

public class DefaultCSVReaderFactory implements CSVReaderFactory {
    private DefaultCSVReaderFactory() {
    }

    private static class SingletonHelper {
        private static final CSVReaderFactory INSTANCE = new DefaultCSVReaderFactory();
    }

    public static CSVReaderFactory getInstance() {
        return DefaultCSVReaderFactory.SingletonHelper.INSTANCE;
    }

    @Override
    public <T> AbstractCSVReader<T> createCSVReader(final Class<T> tClass,
                                                    final String filePath) {
        if (tClass.equals(EmployeeDto.class)) {
            return CSVTypeRecord.EMPLOYEE.make(filePath);
        }
        return null;
    }
}
