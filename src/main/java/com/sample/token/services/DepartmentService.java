package com.sample.token.services;

import com.sample.token.entities.Department;
import com.sample.token.entities.Employee;
import com.sample.token.repository.DepartmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepo departmentRepo;
    @Autowired
    private EmployeeService employeeService;
    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<String> saveDepartment(Department department){
        try {
            int size = department.getEmployees().size();
            for (int i = 0; i < size; i++) {
                String fname = department.getEmployees().get(i).getFirstName();
                String lname = department.getEmployees().get(i).getLastName();
                if (employeeService.isPresent(fname, lname)) {
                    department.getEmployees().get(i).getEmployeePrimeDetails().setPassWord(
                            passwordEncoder.encode(department.getEmployees().get(i).getEmployeePrimeDetails().getPassWord()));
                    departmentRepo.save(department);
                    return ResponseEntity.status(201).body(department.getEmployees().get(i).getFirstName() + " save records successfully");
                } else
                    return ResponseEntity.status(302).body("record already present");
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().body("something went wrong");
        }
       return ResponseEntity.badRequest().body("something went wrong");
    }
}
