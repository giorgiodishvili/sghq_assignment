package org.example.mapper;

import org.example.dto.EmployeeDto;
import org.example.model.Employee;

public class EmployeeMapper {
    private EmployeeMapper() {
    }

    private static class SingletonHelper {
        private static final EmployeeMapper INSTANCE = new EmployeeMapper();
    }

    public static EmployeeMapper getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public Employee map(final EmployeeDto employeeDto,
                        final Employee manager) {
        return new Employee(employeeDto.id(), employeeDto.firstName(),
                employeeDto.lastName(), employeeDto.salary(), manager);
    }

}
