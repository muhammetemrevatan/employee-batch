package com.memrevatan.employeebatch.repository;

import com.memrevatan.employeebatch.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

}
