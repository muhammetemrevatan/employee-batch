package com.memrevatan.employeebatch.service;

import com.memrevatan.employeebatch.data.dto.EmployeeDto;
import com.memrevatan.employeebatch.data.service.EmployeeDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeDataService employeeDataService;

    public void saveEmployee(EmployeeDto employeeDto) {
        employeeDataService.saveEmployee(employeeDto);
    }

    public List<EmployeeDto> getAllEmployees() {
        return employeeDataService.getAllEmployees();
    }
}
