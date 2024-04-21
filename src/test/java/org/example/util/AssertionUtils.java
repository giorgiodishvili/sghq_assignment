package org.example.util;

import org.example.dto.EmployeeDto;
import org.example.model.Employee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class AssertionUtils {
    public static void assertSort(final int size, final List<EmployeeDto> employees) {
        assertSort(size, employees, new Long[]{});
    }

    public static void assertEmployee(final Employee expected, final Employee actual) {
        if (expected == null) return;
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getSalary(), actual.getSalary());
        assertEmployee(expected.getManager(), actual.getManager());
    }

    public static void assertSort(final int size, final List<EmployeeDto> employees, final Long... missing) {
        assertEquals(size, employees.size());

        final AtomicBoolean isManagerPresent = new AtomicBoolean(false);
        final List<Long> traversedEmployees = new ArrayList<>(Arrays.asList(missing)) {
        };

        for (EmployeeDto employee : employees) {
            Optional<Long> managerIdOptional = employee.managerId();
            managerIdOptional.ifPresentOrElse((managerId) -> {
                // Assert that manager Id are ahead of employees
                assertTrue("Manager IDs are not in ahead of employees managerId: " +
                                managerId + " employeeId: " + employee.id(),
                        traversedEmployees.contains(managerId));
                traversedEmployees.add(employee.id());
                isManagerPresent.set(true);
            }, () -> {
                // Assert that employee without managers come first
                assertFalse("Manager already present before CEO for id: " + employee.id(), isManagerPresent.get());
                traversedEmployees.add(employee.id());
            });
        }
    }
}
