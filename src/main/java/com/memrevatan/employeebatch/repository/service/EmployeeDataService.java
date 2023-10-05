package com.memrevatan.employeebatch.repository.service;

import com.memrevatan.employeebatch.dto.EmployeeDto;
import com.memrevatan.employeebatch.entity.Employee;
import com.memrevatan.employeebatch.mapper.EmployeeMapper;
import com.memrevatan.employeebatch.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeDataService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper mapper;

    @Transactional
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return mapper.toDto(employees);
    }

    @Transactional
    public void saveEmployee(EmployeeDto employeeDto) {
        Employee employee = mapper.toEntity(employeeDto);
        employeeRepository.save(employee);
    }
}
