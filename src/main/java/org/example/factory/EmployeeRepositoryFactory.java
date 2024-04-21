package org.example.factory;

import org.example.factory.enums.DbType;
import org.example.repository.EmployeeRepository;

public interface EmployeeRepositoryFactory {
    EmployeeRepository getRepository(DbType type);
}
