package org.example.factory.enums;

import org.example.dto.EmployeeDto;
import org.example.csv.AbstractCSVReader;
import org.example.csv.impl.EmployeeCSVReader;

public enum CSVTypeRecord {
    EMPLOYEE {
        @Override
        public AbstractCSVReader<EmployeeDto> make(String filePath) {
            return new EmployeeCSVReader(filePath);
        }
    };

    public abstract <T> AbstractCSVReader<T> make(String filePath);
}
