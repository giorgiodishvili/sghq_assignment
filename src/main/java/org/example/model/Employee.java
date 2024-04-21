package org.example.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private BigDecimal salary;
    private Employee manager;
    private Set<Employee> subordinateEmployees = new HashSet<>(0);

    public Employee(Long id, String firstName, String lastName, BigDecimal salary, Employee manager) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.manager = manager;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public Set<Employee> getSubordinateEmployees() {
        return subordinateEmployees;
    }

    public void setSubordinateEmployees(Set<Employee> subordinateEmployees) {
        this.subordinateEmployees = subordinateEmployees;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", salary=" + salary +
                ", managerId=" + (manager == null ? null : manager.getId().toString()) +
                '}';
    }
}
