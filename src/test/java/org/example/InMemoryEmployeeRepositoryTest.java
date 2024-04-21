package org.example;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.example.model.Employee;
import org.example.repository.EmployeeRepository;
import org.example.repository.impl.InMemoryEmployeeRepository;
import org.example.util.AssertionUtils;
import org.example.util.BuilderUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class InMemoryEmployeeRepositoryTest
        extends TestCase {
    private final EmployeeRepository employeeRepository;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public InMemoryEmployeeRepositoryTest(String testName) {
        super(testName);
        this.employeeRepository = InMemoryEmployeeRepository.getInstance();
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(InMemoryEmployeeRepositoryTest.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        employeeRepository.deleteAll();
    }

    public void testEmployeeRepositorySaveAndGet() {
        // Given
        final long employeeId = 1L;
        Employee employee = BuilderUtils.EmployeeBuilder.build(employeeId);

        // When
        employeeRepository.save(employee);

        // Then
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        Assert.assertTrue(employeeOptional.isPresent());
        AssertionUtils.assertEmployee(employee, employeeOptional.get());
        AssertionUtils.assertEmployee(employee, employeeRepository.getById(employeeId));
    }

    public void testEmployeeRepositorySaveAndFindAll() {
        // Given
        final long employeeId = 1L;
        final long employeeIdSecond = 2L;
        Employee employee = BuilderUtils.EmployeeBuilder.build(employeeId);
        Employee employeeSecond = BuilderUtils.EmployeeBuilder.build(employeeIdSecond);

        // When
        employeeRepository.save(employee);
        employeeRepository.save(employeeSecond);

        // Then
        Collection<Employee> employees = employeeRepository.findAll();
        Assert.assertEquals(2, employees.size());
        Assert.assertTrue(employees.stream().allMatch(emp -> List.of(employeeId, employeeIdSecond).contains(emp.getId())));
    }

    public void testEmployeeRepositorySaveOrUpdate() {
        // Given
        final long employeeId = 1L;
        final long employeeIdSecond = 2L;
        final long employeeIdThird = 3L;
        final Employee employee = BuilderUtils.EmployeeBuilder.build(employeeId);
        final Employee employeeSecond = BuilderUtils.EmployeeBuilder.build(employeeIdSecond, employee);
        final Employee employeeThird = BuilderUtils.EmployeeBuilder.build(employeeIdThird, employeeSecond);
        employeeRepository.save(employee);
        employeeRepository.save(employeeSecond);
        employeeRepository.save(employeeThird);

        // When
        employeeRepository.save(BuilderUtils.EmployeeBuilder.build(employeeIdThird, employee));

        // Then
        Collection<Employee> employees = employeeRepository.findAll();
        Assert.assertEquals(3, employees.size());
        Assert.assertTrue(employees.stream().allMatch(emp ->
                List.of(employeeId, employeeIdSecond, employeeIdThird).contains(emp.getId())));

        final Employee employee1 = employeeRepository.getById(1L);
        final Employee employee2 = employeeRepository.getById(2L);
        final Employee employee3 = employeeRepository.getById(3L);
        Assert.assertEquals(employee.getId(), employee3.getManager().getId());
        Assert.assertEquals(2, employee1.getSubordinateEmployees().size());
        Assert.assertEquals(0, employee2.getSubordinateEmployees().size());
        Assert.assertEquals(0, employee3.getSubordinateEmployees().size());
    }
}
