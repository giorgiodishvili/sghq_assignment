package org.example;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.example.dto.AnalyzeSalaryDto;
import org.example.dto.EmployeeDto;
import org.example.dto.MaxHierarchyDto;
import org.example.mapper.EmployeeMapper;
import org.example.model.Employee;
import org.example.repository.EmployeeRepository;
import org.example.repository.impl.InMemoryEmployeeRepository;
import org.example.service.EmployeeService;
import org.example.service.impl.EmployeeServiceImpl;
import org.example.util.BuilderUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmployeeServiceTest
        extends TestCase {
    private final EmployeeService employeeService;
    private final EmployeeRepository repository;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public EmployeeServiceTest(String testName) {
        super(testName);
        this.repository = InMemoryEmployeeRepository.getInstance();
        this.employeeService =
                new EmployeeServiceImpl(repository, EmployeeMapper.getInstance());
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(EmployeeServiceTest.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        repository.deleteAll();
    }

    public void testSaveAndFind() {
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
        final Map<Long, List<EmployeeDto>> employeeDtoMap =
                employeeDtoList.stream().collect(Collectors.groupingBy(EmployeeDto::id));

        // When
        employeeService.saveAll(employeeDtoList);

        // Then
        final Collection<Employee> employeesResult = employeeService.findAll();
        Assert.assertEquals(employeeDtoList.size(), employeesResult.size());

        Assert.assertTrue(employeesResult.stream()
                .allMatch(employee -> {
                    List<EmployeeDto> employeeDtos = employeeDtoMap.get(employee.getId());
                    Assert.assertEquals(1, employeeDtos.size());
                    return employeeDtos.get(0).managerId()
                            .map(managerId -> managerId.equals(employee.getManager().getId()))
                            .orElse(true);
                }));
    }

    public void testAnalyzeDepth() {
        // Given
        final List<EmployeeDto> employeeDtoList = List.of(
                BuilderUtils.EmployeeDtoBuilder.buildCeo(123L),
                BuilderUtils.EmployeeDtoBuilder.build(124L, 123L),
                BuilderUtils.EmployeeDtoBuilder.build(125L, 123L),
                BuilderUtils.EmployeeDtoBuilder.build(300L, 124L),
                BuilderUtils.EmployeeDtoBuilder.build(305L, 300L)
        );


        employeeService.saveAll(employeeDtoList);
        // When
        final List<MaxHierarchyDto> maxHierarchyDtos =
                employeeService.analyzeDepth(1);

        // Then
        Assert.assertEquals(2, maxHierarchyDtos.size());
        Assert.assertEquals(3, maxHierarchyDtos.stream().filter(it -> it.employeeId() == 305).findFirst().get().depth());
        Assert.assertEquals(2, maxHierarchyDtos.stream().filter(it -> it.employeeId() == 300).findFirst().get().depth());
    }

    public void testAnalyzeDepthExclusionCheck() {
        // Given
        final List<EmployeeDto> employeeDtoList = List.of(
                BuilderUtils.EmployeeDtoBuilder.buildCeo(123L),
                BuilderUtils.EmployeeDtoBuilder.build(124L, 123L),
                BuilderUtils.EmployeeDtoBuilder.build(125L, 123L),
                BuilderUtils.EmployeeDtoBuilder.build(300L, 124L),
                BuilderUtils.EmployeeDtoBuilder.build(305L, 300L)
        );


        employeeService.saveAll(employeeDtoList);
        // When
        final List<MaxHierarchyDto> maxHierarchyDtos =
                employeeService.analyzeDepth(3);

        // Then
        Assert.assertEquals(0, maxHierarchyDtos.size());
    }

    public void testAnalyzeSalary() {
        // Given
        final List<EmployeeDto> employeeDtoList = List.of(
                BuilderUtils.EmployeeDtoBuilder.buildCeo(123L, new BigDecimal(1000)),
                BuilderUtils.EmployeeDtoBuilder.build(124L, new BigDecimal(100), 123L),
                BuilderUtils.EmployeeDtoBuilder.build(125L, new BigDecimal(100), 123L),
                BuilderUtils.EmployeeDtoBuilder.build(300L, new BigDecimal(40), 124L),
                BuilderUtils.EmployeeDtoBuilder.build(305L, new BigDecimal(30), 300L)
        );
        employeeService.saveAll(employeeDtoList);

        // When
        final List<AnalyzeSalaryDto> analyzeSalaryDtos =
                employeeService.analyzeSalary(1.2, 1.5);

        // Then
        Assert.assertEquals(2, analyzeSalaryDtos.size());

        Assert.assertEquals(BigDecimal.valueOf(150.0), analyzeSalaryDtos.stream().filter(it -> it.employeeId() == 123).findFirst().get().expectedSalary());
        Assert.assertEquals(BigDecimal.valueOf(60.0), analyzeSalaryDtos.stream().filter(it -> it.employeeId() == 124).findFirst().get().expectedSalary());
    }

    public void testAnalyzeSalaryInclusiveMax() {
        // Given
        final List<EmployeeDto> employeeDtoList = List.of(
                BuilderUtils.EmployeeDtoBuilder.buildCeo(123L, new BigDecimal(200)),
                BuilderUtils.EmployeeDtoBuilder.build(124L, new BigDecimal(100), 123L),
                BuilderUtils.EmployeeDtoBuilder.build(125L, new BigDecimal(100), 123L)
        );
        employeeService.saveAll(employeeDtoList);

        // When
        final List<AnalyzeSalaryDto> analyzeSalaryDtos =
                employeeService.analyzeSalary(1.0, 2.0);

        // Then
        Assert.assertEquals(0, analyzeSalaryDtos.size());
    }

    public void testAnalyzeSalaryInclusiveMin() {
        // Given
        final List<EmployeeDto> employeeDtoList = List.of(
                BuilderUtils.EmployeeDtoBuilder.buildCeo(123L, new BigDecimal(100)),
                BuilderUtils.EmployeeDtoBuilder.build(124L, new BigDecimal(100), 123L),
                BuilderUtils.EmployeeDtoBuilder.build(125L, new BigDecimal(100), 123L)
        );
        employeeService.saveAll(employeeDtoList);

        // When
        final List<AnalyzeSalaryDto> analyzeSalaryDtos =
                employeeService.analyzeSalary(1.0, 2.0);

        // Then
        Assert.assertEquals(0, analyzeSalaryDtos.size());
    }
}
