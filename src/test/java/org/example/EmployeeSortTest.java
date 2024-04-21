package org.example;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.example.dto.EmployeeDto;
import org.example.sort.EmployeeSort;
import org.example.sort.impl.EmployeeManagerHierarchySort;
import org.example.util.AssertionUtils;
import org.example.util.BuilderUtils;

import java.util.List;

public class EmployeeSortTest
        extends TestCase {

    private final EmployeeSort employeeSort;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public EmployeeSortTest(String testName) {
        super(testName);
        employeeSort = EmployeeManagerHierarchySort.getInstance();
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(EmployeeSortTest.class);
    }

    public void testEmployeeDtoWithMixedIdSort() {
        // Given
        final List<EmployeeDto> employeeDtoList = List.of(
                BuilderUtils.EmployeeDtoBuilder.build(300L, 124L),
                BuilderUtils.EmployeeDtoBuilder.build(124L, 123L),
                BuilderUtils.EmployeeDtoBuilder.buildCeo(123L),
                BuilderUtils.EmployeeDtoBuilder.build(125L, 123L),
                BuilderUtils.EmployeeDtoBuilder.build(305L, 306L),
                BuilderUtils.EmployeeDtoBuilder.build(306L, 300L),
                BuilderUtils.EmployeeDtoBuilder.build(307L, 303L),
                BuilderUtils.EmployeeDtoBuilder.build(308L, 309L),
                BuilderUtils.EmployeeDtoBuilder.build(303L, 308L),
                BuilderUtils.EmployeeDtoBuilder.build(309L, 124L)
        );

        // When
        final List<EmployeeDto> employees =
                employeeSort.sort(employeeDtoList);

        // Then
        AssertionUtils.assertSort(10, employees);
    }

    public void testSimpleUserBaseSort() {
        // Given
        final List<EmployeeDto> employeeDtoList = List.of(
                BuilderUtils.EmployeeDtoBuilder.buildCeo(123L),
                BuilderUtils.EmployeeDtoBuilder.build(124L, 123L),
                BuilderUtils.EmployeeDtoBuilder.build(125L, 123L),
                BuilderUtils.EmployeeDtoBuilder.build(300L, 124L),
                BuilderUtils.EmployeeDtoBuilder.build(305L, 300L)
        );

        // When
        final List<EmployeeDto> employees =
                employeeSort.sort(employeeDtoList);

        // Then
        AssertionUtils.assertSort(5, employees);
    }

    public void testSortingWithNonExistentManagerId() {
        // Given
        final List<EmployeeDto> employeeDtoList = List.of(
                BuilderUtils.EmployeeDtoBuilder.buildCeo(123L),
                BuilderUtils.EmployeeDtoBuilder.build(124L, 123L),
                BuilderUtils.EmployeeDtoBuilder.build(125L, 123L),
                BuilderUtils.EmployeeDtoBuilder.build(300L, 124L),
                BuilderUtils.EmployeeDtoBuilder.build(305L, 306L)
        );

        // When
        final List<EmployeeDto> employees =
                employeeSort.sort(employeeDtoList);

        // Then
        AssertionUtils.assertSort(5, employees, 306L);
    }
}
