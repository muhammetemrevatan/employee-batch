package com.memrevatan.employeebatch.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "EMPLOYEE_DETAIL", schema = "BATCH")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetail implements Serializable {
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
