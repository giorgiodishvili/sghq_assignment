package org.example.factory.impl;

import org.example.factory.EmployeeRepositoryFactory;
import org.example.factory.EmployeeServiceFactory;
import org.example.factory.EmployeeSortFactory;
import org.example.factory.enums.DbType;
import org.example.mapper.EmployeeMapper;
import org.example.service.EmployeeService;
import org.example.service.impl.EmployeeServiceImpl;

public class DefaultEmployeeServiceFactory implements EmployeeServiceFactory {
    private DefaultEmployeeServiceFactory() {
    }

    private static class SingletonHelper {
        private static final EmployeeServiceFactory INSTANCE =
                new DefaultEmployeeServiceFactory();
    }

    public static EmployeeServiceFactory getInstance() {
        return DefaultEmployeeServiceFactory.SingletonHelper.INSTANCE;
    }

    @Override
    public EmployeeService createConfiguredService(final DbType dbType) {
        final EmployeeRepositoryFactory repositoryFactory =
                DefaultEmployeeRepositoryFactory.getInstance();

        final EmployeeSortFactory employeeSortFactory =
                DefaultEmployeeSortFactory.getInstance();

        return new EmployeeServiceImpl(repositoryFactory.getRepository(dbType),
                EmployeeMapper.getInstance(), employeeSortFactory.createEmployeeSort());
    }

    @Override
    public EmployeeService createService() {
        return createConfiguredService(DbType.IN_MEMORY);
    }
}
