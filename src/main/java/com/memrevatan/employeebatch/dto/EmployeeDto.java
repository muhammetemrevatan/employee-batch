package com.memrevatan.employeebatch.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class EmployeeDto implements Serializable {
    private String guid;
    private String name;
    private String country;
    private String gender;
    private BigDecimal salary;
}
