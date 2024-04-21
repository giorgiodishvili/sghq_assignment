package org.example.service.impl;

import org.example.dto.AnalyzeSalaryDto;
import org.example.dto.EmployeeDto;
import org.example.dto.MaxHierarchyDto;
import org.example.logger.AppLogger;
import org.example.mapper.EmployeeMapper;
import org.example.model.Employee;
import org.example.repository.EmployeeRepository;
import org.example.service.EmployeeService;
import org.example.sort.impl.EmployeeManagerHierarchySort;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public Collection<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public void save(final EmployeeDto employeeDto) {
        AppLogger.logFine("Call [save] employeeDto: %s".formatted(employeeDto));
        employeeRepository.save(map(employeeDto));
    }

    /**
     * ASSUMPTION is that List can be out of order.
     * in a way where employeeId X can have manager with id of X+Y
     *
     * @param employeeDtos
     */
    @Override
    public void saveAll(final List<EmployeeDto> employeeDtos) {
        EmployeeManagerHierarchySort.getInstance().sort(employeeDtos).forEach(this::save);
    }

    private Employee map(EmployeeDto employeeDto) {
        final Optional<Employee> managerOptional =
                employeeDto.managerId().map(employeeRepository::getById);

        final Employee employee = employeeMapper.map(employeeDto, managerOptional.orElse(null));

        if (managerOptional.isPresent()) {
            final var manager = managerOptional.get();
            if (manager.getSubordinateEmployees() == null) {
                manager.setSubordinateEmployees(new HashSet<>());
            }
            manager.getSubordinateEmployees().add(employee);
        }
        return employee;
    }

    /**
     * iterates whole employee list, calculates the average salary of employee subordinates
     * and then calculates if the users salary meets the range
     *
     * @param minSalaryPercentage inclusive min average salary
     * @param maxSalaryPercentage inclusive max average salary
     * @return List of AnalyzeSalaryDto
     */
    @Override
    public List<AnalyzeSalaryDto> analyzeSalary(final double minSalaryPercentage,
                                                final double maxSalaryPercentage) {
        AppLogger.logInfo("Call [analyzeSalary]");
        return employeeRepository.findAll()
                .parallelStream()
                .map(manager -> {
                    if (manager.getSubordinateEmployees() != null &&
                            !manager.getSubordinateEmployees().isEmpty()) {

                        final BigDecimal total = manager.getSubordinateEmployees()
                                .stream()
                                .map(Employee::getSalary)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        final BigDecimal average =
                                total.divide(new BigDecimal(manager.getSubordinateEmployees().size()),
                                        RoundingMode.HALF_UP);
                        final BigDecimal minRequiredSalary = average.multiply(BigDecimal.valueOf(minSalaryPercentage));
                        final BigDecimal maxAllowedSalary = average.multiply(BigDecimal.valueOf(maxSalaryPercentage));

                        if (manager.getSalary().compareTo(minRequiredSalary) < 0) {
                            final BigDecimal minSalary = minRequiredSalary.subtract(manager.getSalary());
                            return new AnalyzeSalaryDto(manager.getId(), manager.getSalary(), minRequiredSalary,
                                    "%s %s earns less than required by $%s"
                                            .formatted(manager.getFirstName(), manager.getLastName(),
                                                    minSalary));
                        } else if (manager.getSalary().compareTo(maxAllowedSalary) > 0) {
                            final BigDecimal maxSalary = manager.getSalary().subtract(maxAllowedSalary);
                            return new AnalyzeSalaryDto(manager.getId(), manager.getSalary(), maxAllowedSalary,
                                    "%s %s earns more than allowed by $%s"
                                            .formatted(manager.getFirstName(), manager.getLastName(),
                                                    maxSalary));
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * iterates whole employee list, and goes into depth of each users manager, untill we get to CEO
     * @param maxDepthAllowed exclusive if there is exactly maxDepthAllowed they do not violate.
     * @return
     */
    @Override
    public List<MaxHierarchyDto> analyzeDepth(final long maxDepthAllowed) {
        AppLogger.logInfo("Call [analyzeDepth]");
        return employeeRepository.findAll()
                .parallelStream()
                .map(emp -> {
                    long depth = 0;
                    Employee current = emp;
                    while (current.getManager() != null) {
                        depth++;
                        current = current.getManager();
                    }
                    if (depth > maxDepthAllowed) {
                        return new MaxHierarchyDto(emp.getId(), depth,
                                "%s %s has a reporting line that is too long by %s"
                                        .formatted(emp.getFirstName(), emp.getLastName(), depth - maxDepthAllowed));
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
