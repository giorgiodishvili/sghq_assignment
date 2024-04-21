package org.example;

import org.example.csv.AbstractCSVReader;
import org.example.dto.EmployeeDto;
import org.example.factory.impl.DefaultCSVReaderFactory;
import org.example.factory.impl.DefaultEmployeeServiceFactory;
import org.example.logger.AppLogger;
import org.example.service.EmployeeService;

import java.util.List;

public class App {

    public static void main(String[] args) {
        AppLogger.logInfo("Application Started");
        long maxDepthAllowed = 4;
        double minSalaryPercentage = 1.2d;
        double maxSalaryPercentage = 1.5d;

        if (args.length < 1) {
            AppLogger.logInfo("Please Provide ${PathToFile}");
            AppLogger.logInfo("Example: \n %s"
                    .formatted("/root/employee.csv"));
            return;
        }

        final String filePath = args[0];

        if (args.length > 1) {
            AppLogger.logInfo("maxDepthAllowed: %d provided".formatted(maxDepthAllowed));
            maxDepthAllowed = Long.parseLong(args[1]);
        } else {
            AppLogger.logInfo("Using Default maxDepthAllowed: %d".formatted(maxDepthAllowed));
        }

        if (args.length > 3) {
            AppLogger.logInfo(("minSalaryPercentage: %f and maxSalaryPercentage: %f provided")
                    .formatted(minSalaryPercentage, maxSalaryPercentage));
            minSalaryPercentage = Double.parseDouble(args[2]);
            maxSalaryPercentage = Double.parseDouble(args[3]);
        } else {
            AppLogger.logInfo(("Using Default minSalaryPercentage: %f and maxSalaryPercentage: %f")
                    .formatted(minSalaryPercentage, maxSalaryPercentage));
        }

        final AbstractCSVReader<EmployeeDto> csvReader = DefaultCSVReaderFactory.getInstance()
                .createCSVReader(EmployeeDto.class, filePath);

        final EmployeeService service =
                DefaultEmployeeServiceFactory.getInstance()
                        .createService();

        final List<EmployeeDto> employeeDtos = csvReader.readCSV();

        service.saveAll(employeeDtos);

        service.analyzeDepth(maxDepthAllowed)
                .forEach(dto -> AppLogger.logInfo(dto.message()));

        service.analyzeSalary(minSalaryPercentage, maxSalaryPercentage)
                .forEach(dto -> AppLogger.logInfo(dto.message()));

    }
}
