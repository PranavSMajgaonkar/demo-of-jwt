package com.sample.token.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@AllArgsConstructor
@ToString
@Table
public class EmployeeAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long employeeAddressId;
    private String addressType;
    private String address;
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "fk_employeeId")
//    private Employee employee;
}
