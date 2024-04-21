package org.example;

import org.example.csv.AbstractCSVReader;
import org.example.dto.EmployeeDto;
import org.example.factory.impl.DefaultCSVReaderFactory;
import org.example.factory.impl.DefaultEmployeeServiceFactory;
import org.example.logger.AppLogger;
import org.example.service.EmployeeService;

import java.nio.file.Paths;
import java.util.List;

public class App {

    public static void main(String[] args) {
        AppLogger.logInfo("Application Started");
        final String fileNameInResource = "employee.csv";

        final AbstractCSVReader<EmployeeDto> csvReader = DefaultCSVReaderFactory.getInstance()
                .createCSVReader(EmployeeDto.class, Paths.get("src/main/resources/", fileNameInResource)
                        .toAbsolutePath().toString());

        final EmployeeService service =
                DefaultEmployeeServiceFactory.getInstance()
                        .createService();

        final List<EmployeeDto> employeeDtos = csvReader.readCSV();

        service.saveAll(employeeDtos);

        service.analyzeDepth(4)
                .forEach(dto -> AppLogger.logInfo(dto.message()));

        service.analyzeSalary(1.2, 1.5)
                .forEach(dto -> AppLogger.logInfo(dto.message()));

    }
}
