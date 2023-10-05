package com.memrevatan.employeebatch.mapper;

import com.memrevatan.employeebatch.dto.EmployeeDto;
import com.memrevatan.employeebatch.entity.Employee;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class EmployeeMapper {
    public Employee toEntity(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        employee.setGuid(UUID.randomUUID().toString());
        employee.setName(employeeDto.getName());
        employee.setCountry(employeeDto.getCountry());
        employee.setGender(employeeDto.getGender());
        employee.setSalary(employeeDto.getSalary());
        return employee;
    }

    public List<EmployeeDto> toDto(List<Employee> employees) {
        List<EmployeeDto> employeeDtoList = new ArrayList<>();
        for (Employee employee: employees) {
            EmployeeDto employeeDto = new EmployeeDto();
            employeeDto.setGuid(employee.getGuid());
            employeeDto.setName(employee.getName());
            employeeDto.setCountry(employee.getCountry());
            employeeDto.setSalary(employee.getSalary());
            employeeDto.setGender(employee.getGender());
            employeeDtoList.add(employeeDto);
        }
        return employeeDtoList;
    }
}
