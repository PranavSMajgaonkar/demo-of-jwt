package com.sample.token.controller;

import com.sample.token.entities.Department;
import com.sample.token.model.UserDetailsWraper;
import com.sample.token.services.DepartmentService;
import com.sample.token.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("admin")
@CrossOrigin(origins = "http://localhost:3000")

public class AdminAccessController {
    @Autowired
    private UserService userService;
    @Autowired
    private DepartmentService departmentService;


    @PostMapping("registerByAdmin")
    public ResponseEntity<String> registerEmployee(@RequestBody Department department){
        return departmentService.saveDepartment(department);
    }

    @GetMapping("user-list")
    public ResponseEntity<Object> getAllUserList(){
        List<UserDetailsWraper> userList = userService.retrieveAllUserList();
       return ResponseEntity.ok().body(userList);
    }
}
