package com.sample.token.controller;

import com.sample.token.entities.UserDetails;
import com.sample.token.model.UserDetailsWraper;
import com.sample.token.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("admin")
@CrossOrigin(origins = "http://localhost:3000")

public class AdminAccessController {
    @Autowired
    private UserService userService;
    @GetMapping("user-list")
    public ResponseEntity<Object> getAllUserList(){
        List<UserDetailsWraper> userList = userService.retrieveAllUserList();
       return ResponseEntity.ok().body(userList);
    }
}
