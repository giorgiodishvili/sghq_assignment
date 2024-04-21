package org.example.util;

import org.example.dto.EmployeeDto;
import org.example.model.Employee;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.Random;

public final class BuilderUtils {

    public static final class EmployeeBuilder {
        public static Employee build(Long id, String firstName, String lastName, BigDecimal salary, Employee manager) {
            return new Employee(id, firstName, lastName, salary, manager);
        }

        public static Employee build(Long id, BigDecimal salary, Employee manager) {
            return build(id, generateRandomWord(5),
                    generateRandomWord(10), salary, manager);
        }

        public static Employee build(Long id, Employee manager) {
            return build(id, generateSalary(), manager);
        }

        public static Employee build(Long id) {
            return build(id, null);
        }
    }

    public static final class EmployeeDtoBuilder {
        public static EmployeeDto build(Long id, String firstName, String lastName,
                                        BigDecimal salary, Long managerId) {
            return new EmployeeDto(id, firstName, lastName, Optional.ofNullable(managerId), salary);
        }

        public static EmployeeDto build(Long id, BigDecimal salary, Long managerId) {
            return build(id, generateRandomWord(5),
                    generateRandomWord(10), salary, managerId);
        }

        public static EmployeeDto build(Long id, Long managerId) {
            return build(id, generateSalary(), managerId);
        }

        public static EmployeeDto buildCeo(Long id, BigDecimal salary) {
            return build(id, salary, null);
        }

        public static EmployeeDto buildCeo(Long id) {
            return build(id, null);
        }
    }

    static String generateRandomWord(int wordLength) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(wordLength);
        for (int i = 0; i < wordLength; i++) {
            char tmp = (char) ('a' + r.nextInt('z' - 'a'));
            sb.append(tmp);
        }
        return sb.toString();
    }

    static BigDecimal generateSalary() {
        return BigDecimal.valueOf(new Random().nextDouble() * 10000)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
