package com.sample.token.services;

import com.sample.token.entities.Employee;
import com.sample.token.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepo employeeRepo;

    public boolean isPresent(String firstname, String lastname){
        Employee emp = employeeRepo.findByFirstNameAndLastName(firstname,lastname);
        return emp == null;
    }

}
