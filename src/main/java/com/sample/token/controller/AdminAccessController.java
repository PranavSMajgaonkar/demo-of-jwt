package com.sample.token.controller;

import com.sample.token.entities.UserDetails;
import com.sample.token.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("admin")
public class AdminAccessController {
    @Autowired
    private UserService userService;
    @GetMapping("/user-list")
    public ResponseEntity<Object> getAllUserList(){
        return ResponseEntity.ok().body(userService.retrieveAllUserList());
    }
}
