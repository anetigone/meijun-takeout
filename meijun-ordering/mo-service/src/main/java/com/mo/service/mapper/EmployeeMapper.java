package com.mo.service.mapper;

import com.mo.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    Employee getEmployeeByUsername(@Param("username") String username);
    Employee getEmployeeByUuid(@Param("uuid") String uuid);
    Employee getEmployeeById(@Param("id") Long id);

    List<Employee> getAll();

    List<Employee> getPage(@Param("offset") int offset, @Param("size") int size);

    void saveEmployee(@Param("employee") Employee employee);

    void updateEmployee(@Param("employee") Employee employee);

    void deleteEmployee(@Param("id") Long id);
    
    int getEmployeeCount();
}
