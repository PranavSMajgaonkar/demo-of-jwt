package com.sample.token.repository;

import com.sample.token.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Long> {
    Employee  findByFirstNameAndLastName(String firstname, String lastname);
}
