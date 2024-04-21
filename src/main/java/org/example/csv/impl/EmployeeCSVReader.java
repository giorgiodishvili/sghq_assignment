package org.example.csv.impl;

import org.example.constant.AppConstants;
import org.example.dto.EmployeeDto;
import org.example.csv.AbstractCSVReader;

import java.math.BigDecimal;
import java.util.Optional;

public class EmployeeCSVReader extends AbstractCSVReader<EmployeeDto> {
    public EmployeeCSVReader(String filePath) {
        super(filePath);
    }

    @Override
    protected EmployeeDto processLine(final String line) {
        final var values = line.split(AppConstants.COMMA_DELIMITER);

        if (values.length == 4) {
            return new EmployeeDto(Long.parseLong(values[0]), values[1], values[2],
                    Optional.empty(),
                    new BigDecimal(values[3]));
        }

        return new EmployeeDto(Long.parseLong(values[0]), values[1], values[2],
                Optional.of(values[4]).map(Long::parseLong),
                new BigDecimal(values[3]));
    }
}
