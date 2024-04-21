package org.example.dto;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

public record EmployeeDto(long id, String firstName, String lastName,
                          Optional<Long> managerId, BigDecimal salary) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeDto that = (EmployeeDto) o;
        return Objects.equals(id, that.id);
    }
}
