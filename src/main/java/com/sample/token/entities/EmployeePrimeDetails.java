package com.sample.token.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table
public class EmployeePrimeDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "username", nullable = false)
    private String userName;
    @Column(name = "password", nullable = false)
    private String passWord;
    @Column(nullable = false)
    private String role;
}