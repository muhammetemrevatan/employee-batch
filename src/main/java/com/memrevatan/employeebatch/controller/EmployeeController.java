package com.memrevatan.employeebatch.controller;

import com.memrevatan.employeebatch.dto.EmployeeDto;
import com.memrevatan.employeebatch.entity.Employee;
import com.memrevatan.employeebatch.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public void saveEmployee(@RequestBody EmployeeDto employeeDto) {
        employeeService.saveEmployee(employeeDto);
    }

    @GetMapping
    public List<EmployeeDto> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @PutMapping
    public void startBatch() {
        employeeService.startBatch();
    }
}
