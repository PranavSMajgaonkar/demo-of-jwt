package com.sample.token.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
@Data
@AllArgsConstructor
@ToString
@Entity
@Table
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long employeeId;
    @Column(nullable = false)
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    @Column(nullable = false)
    private Date dateOfHire;
    private String employeePosition;
    @Column(nullable = false)
    private long employeeSalary;
    @Column(nullable = false)
    private long employeePhNumber;
    @Column(nullable = false)
    private String employeeStatus;  //Whether the employee is full-time, part-time,contractor or intern;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_id")
    private EmployeePrimeDetails employeePrimeDetails;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_employeeAddressId")
    private EmployeeAddress employeeAddress;    //employee address is either permanent or current;
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "fk_departmentId")
//    private Department employeeDepartment;

}
