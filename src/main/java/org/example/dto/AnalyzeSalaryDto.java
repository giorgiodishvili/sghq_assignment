package org.example.dto;

import java.math.BigDecimal;

public record AnalyzeSalaryDto(long employeeId, BigDecimal currentSalary,
                               BigDecimal expectedSalary, String message) {

}
