package com.memrevatan.employeebatch.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "EMPLOYEE", schema = "BATCH")
@Getter
@Setter
public class Employee {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "guid", updatable = false, nullable = false)
    private String guid;
    private String name;
    private String country;
    private String gender;
    private BigDecimal salary;
}
