package org.example;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.example.csv.AbstractCSVReader;
import org.example.csv.impl.EmployeeCSVReader;
import org.example.dto.EmployeeDto;

import java.nio.file.Paths;
import java.util.List;

public class CsvReaderTest
        extends TestCase {
    private final AbstractCSVReader<EmployeeDto> csvReader;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public CsvReaderTest(String testName) {
        super(testName);
        this.csvReader = new EmployeeCSVReader(Paths.get("src/test/resources/", "employee.csv").toAbsolutePath().toString());
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(CsvReaderTest.class);
    }

    public void testReadCSV() {
        // When
        final List<EmployeeDto> employees = csvReader.readCSV();

        // Then
        Assert.assertEquals(10, employees.size());
    }
}
