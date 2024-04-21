package org.example.factory.impl;

import org.example.factory.EmployeeRepositoryFactory;
import org.example.factory.enums.DbType;
import org.example.repository.EmployeeRepository;
import org.example.repository.impl.InMemoryEmployeeRepository;

import java.util.Objects;

public class DefaultEmployeeRepositoryFactory implements EmployeeRepositoryFactory {

    private DefaultEmployeeRepositoryFactory() {
    }

    private static class SingletonHelper {
        private static final EmployeeRepositoryFactory INSTANCE = new DefaultEmployeeRepositoryFactory();
    }

    public static EmployeeRepositoryFactory getInstance() {
        return DefaultEmployeeRepositoryFactory.SingletonHelper.INSTANCE;
    }

    @Override
    public EmployeeRepository getRepository(DbType type) {
        if (Objects.equals(type, DbType.IN_MEMORY)) {
            return InMemoryEmployeeRepository.getInstance();
        }
        return null;
    }
}
