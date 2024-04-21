package org.example.factory;

import org.example.factory.enums.DbType;
import org.example.service.EmployeeService;

public interface EmployeeServiceFactory {
    EmployeeService createConfiguredService(DbType dbType);

    EmployeeService createService();
}
