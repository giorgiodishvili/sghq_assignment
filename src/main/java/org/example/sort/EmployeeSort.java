package org.example.sort;

import org.example.dto.EmployeeDto;

import java.util.List;

public interface EmployeeSort {
    List<EmployeeDto> sort(final List<EmployeeDto> employees);
}
