package com.mo.service.impl;

import com.mo.api.service.EmployeeService;
import com.mo.common.exception.RegisterFailedException;
import com.mo.entity.Employee;
import com.mo.service.annotation.AutoFillTime;
import com.mo.service.annotation.AutoFillUuid;
import com.mo.service.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;

    @Override
    public List<Employee> getAllEmployee(){
        return employeeMapper.getAll();
    }

    @Override
    public List<Employee> getEmployeePage(int offset, int size){
        return employeeMapper.getPage(offset, size);
    }

    @Override
    @AutoFillTime
    @AutoFillUuid
    public void saveEmployee(Employee employee){
        Employee employee1 = employeeMapper.getEmployeeByUsername(employee.getUsername());
        if(employee1 != null) throw new RegisterFailedException("员工已存在");

        employeeMapper.saveEmployee(employee);
    }

    @Override
    @AutoFillTime
    public void updateEmployee(Employee employee){
        employeeMapper.updateEmployee(employee);
    }

    @Override
    public void deleteEmployee(Long id){
        employeeMapper.deleteEmployee(id);
    }

    @Override
    public int getEmployeeCount() {
        return employeeMapper.getEmployeeCount();
    }
}
