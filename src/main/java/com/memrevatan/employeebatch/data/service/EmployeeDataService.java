package com.memrevatan.employeebatch.data.service;

import com.memrevatan.employeebatch.data.dto.EmployeeDto;
import com.memrevatan.employeebatch.data.entity.Employee;
import com.memrevatan.employeebatch.data.repository.EmployeeRepository;
import com.memrevatan.employeebatch.mapper.EmployeeMapper;
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
