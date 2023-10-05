package com.memrevatan.employeebatch.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "EMPLOYEE_DETAIL", schema = "BATCH")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetail {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "guid", updatable = false, nullable = false)
    private String guid;
    private String name;
    private String currency;
    private String branch;
}
