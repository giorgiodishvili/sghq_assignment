package org.example.repository.impl;

import org.example.exception.ManagerNotFoundException;
import org.example.model.Employee;
import org.example.repository.EmployeeRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryEmployeeRepository implements EmployeeRepository {
    private final Map<Long, Employee> employeeMap = new ConcurrentHashMap<>();

    private InMemoryEmployeeRepository() {
    }

    private static class SingletonHelper {
        private static final EmployeeRepository INSTANCE = new InMemoryEmployeeRepository();
    }

    public static EmployeeRepository getInstance() {
        return InMemoryEmployeeRepository.SingletonHelper.INSTANCE;
    }

    @Override
    public void save(final Employee employee) {
        findById(employee.getId())
                .ifPresentOrElse((existingEmployee) -> {
                    merge(employee, existingEmployee);
                    employeeMap.put(existingEmployee.getId(), existingEmployee);
                }, () -> {
                    reconcileManager(employee);
                    employeeMap.put(employee.getId(), employee);
                });
    }

    @Override
    public Employee getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new ManagerNotFoundException(id));
    }

    @Override
    public Optional<Employee> findById(final Long id) {
        return Optional.ofNullable(employeeMap.get(id));
    }

    @Override
    public Collection<Employee> findAll() {
        return employeeMap.values();
    }

    @Override
    public void deleteAll() {
        employeeMap.clear();
    }

    @Override
    public void deleteById(Long id) {
        employeeMap.remove(id);
    }


    private void merge(final Employee employee,
                       final Employee existingEmployee) {
        // update entity parameters
        existingEmployee.setFirstName(employee.getFirstName());
        existingEmployee.setLastName(employee.getLastName());
        existingEmployee.setSalary(employee.getSalary());

        // skip if managers were not updated
        final Employee currentManager = existingEmployee.getManager();
        final Employee toBeManager = employee.getManager();
        if (Objects.nonNull(currentManager) &&
                Objects.nonNull(toBeManager) &&
                Objects.equals(currentManager.getId(), toBeManager.getId())) {
            return;
        }

        // remove old manager from existing record
        if (currentManager != null) {
            currentManager
                    .getSubordinateEmployees()
                    .remove(existingEmployee);
        }

        // set new manager to existing record
        existingEmployee.setManager(toBeManager);

        // add current entity to new manager
        reconcileManager(existingEmployee);
    }

    private void reconcileManager(final Employee employee) {
        final Employee manager = employee.getManager();

        if (manager != null) {
            if (manager.getSubordinateEmployees() == null) {
                manager.setSubordinateEmployees(new HashSet<>());
            }
            manager.getSubordinateEmployees().add(employee);
        }
    }

}
