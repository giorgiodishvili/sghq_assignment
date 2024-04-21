package org.example.service;

import org.example.dto.AnalyzeSalaryDto;
import org.example.dto.EmployeeDto;
import org.example.dto.MaxHierarchyDto;
import org.example.model.Employee;

import java.util.Collection;
import java.util.List;

public interface EmployeeService {

    Collection<Employee> findAll();

    void save(EmployeeDto employeeDto);

    void saveAll(List<EmployeeDto> employeeDtos);

    List<AnalyzeSalaryDto> analyzeSalary(double minSalaryPercentage, double maxSalaryPercentage);

    List<MaxHierarchyDto> analyzeDepth(long maxDepthAllowed);
}

