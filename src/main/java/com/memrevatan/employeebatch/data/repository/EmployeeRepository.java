package com.memrevatan.employeebatch.data.repository;

import com.memrevatan.employeebatch.data.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

}
