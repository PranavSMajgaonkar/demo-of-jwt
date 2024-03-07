package com.sample.token.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;
@Data
@AllArgsConstructor
@ToString
@Entity
@Table
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long departmentId;
    @Column(nullable = false)
    private String departmentName;
    @OneToMany(mappedBy="department", cascade = CascadeType.ALL)
    private List<Employee> employees;
}
