package com.memrevatan.employeebatch.service;

import com.memrevatan.employeebatch.dto.EmployeeDto;
import com.memrevatan.employeebatch.entity.Employee;
import com.memrevatan.employeebatch.mapper.EmployeeMapper;
import com.memrevatan.employeebatch.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper mapper;
    private final JobLauncher jobLauncher;
    private final Job job;

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

    @Scheduled(fixedRate = 12 * 60 * 60 * 1000)
    public void startBatch() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", new Date().toString())
                .addLong("id", 123L)
                .toJobParameters();

        try {
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
    }
}
