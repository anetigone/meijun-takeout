package com.mo.api.service;

import com.mo.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployee();

    List<Employee> getEmployeePage(int offset, int size);

    void saveEmployee(Employee employee);

    void updateEmployee(Employee employee);

    void deleteEmployee(Long id);

    int getEmployeeCount();

    Employee getEmployeeById(Long id);
}
