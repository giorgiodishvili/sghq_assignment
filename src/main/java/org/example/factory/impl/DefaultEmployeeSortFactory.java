package org.example.factory.impl;

import org.example.factory.EmployeeSortFactory;
import org.example.sort.EmployeeSort;
import org.example.sort.impl.EmployeeManagerHierarchySort;

public class DefaultEmployeeSortFactory implements EmployeeSortFactory {
    private DefaultEmployeeSortFactory() {
    }

    private static class SingletonHelper {
        private static final EmployeeSortFactory INSTANCE =
                new DefaultEmployeeSortFactory();
    }

    public static EmployeeSortFactory getInstance() {
        return DefaultEmployeeSortFactory.SingletonHelper.INSTANCE;
    }

    @Override
    public EmployeeSort createEmployeeSort() {
        return EmployeeManagerHierarchySort.getInstance();
    }
}
