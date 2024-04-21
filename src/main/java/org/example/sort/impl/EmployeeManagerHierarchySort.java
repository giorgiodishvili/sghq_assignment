package org.example.sort.impl;

import org.example.dto.EmployeeDto;
import org.example.sort.EmployeeSort;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class EmployeeManagerHierarchySort implements EmployeeSort {
    private EmployeeManagerHierarchySort() {
    }

    private static class SingletonHelper {
        private static final EmployeeSort INSTANCE = new EmployeeManagerHierarchySort();
    }

    public static EmployeeSort getInstance() {
        return EmployeeManagerHierarchySort.SingletonHelper.INSTANCE;
    }


    /**
     * Public method to sort employees based on their managerial depth.
     * Sort by ascending depth
     */
    @Override
    public List<EmployeeDto> sort(final List<EmployeeDto> employees) {
        final Map<Long, List<EmployeeDto>> managerMap = initializeManagerMap(employees);
        final Map<Long, Integer> managerDepth = calculateDepth(employees, managerMap);

        final List<EmployeeDto> sortedEmployees = new ArrayList<>(employees);
        sortedEmployees.sort((e1, e2) -> {
            int depth1 = managerDepth.getOrDefault(e1.id(), 0);
            int depth2 = managerDepth.getOrDefault(e2.id(), 0);
            return Integer.compare(depth1, depth2);  // Sort by ascending depth
        });

        return sortedEmployees;
    }

    /**
     * Initializes managerMap where only managers are placed.
     */
    private Map<Long, List<EmployeeDto>> initializeManagerMap(final List<EmployeeDto> employees) {
        final Map<Long, List<EmployeeDto>> managerMap = new HashMap<>();
        employees.forEach(emp -> emp.managerId().ifPresent(managerId -> {
            managerMap.putIfAbsent(managerId, new ArrayList<>());
            managerMap.get(managerId).add(emp);
        }));
        return managerMap;
    }

    /**
     * Calculates queue of employees who are not managing anybody
     * and initializes their managerDepth.
     */
    private Map<Long, Integer> calculateDepth(final List<EmployeeDto> employees,
                                              final Map<Long, List<EmployeeDto>> managerMap) {
        final Map<Long, Integer> managerDepth = new HashMap<>();
        final Queue<EmployeeDto> queue = new ArrayDeque<>();

        // Initialize the queue with top-level managers or orphans
        employees.forEach(emp -> {
            if (emp.managerId().isEmpty() || !managerMap.containsKey(emp.id())) {
                queue.add(emp);
                managerDepth.put(emp.id(), 0);  // Top level or orphans
            }
        });

        while (!queue.isEmpty()) {
            EmployeeDto current = queue.poll();
            int currentDepth = managerDepth.get(current.id());
            List<EmployeeDto> subordinates = managerMap.get(current.id());
            if (subordinates != null) {
                subordinates.forEach(subordinate -> {
                    managerDepth.put(subordinate.id(), currentDepth + 1);
                    queue.add(subordinate);
                });
            }
        }

        return managerDepth;
    }
}
