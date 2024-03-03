package com.sample.token.controller;

import com.sample.token.entities.EmployeePrimeDetails;
import com.sample.token.model.UserReq;
import com.sample.token.security.JwtUtil;
import com.sample.token.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("user")
@CrossOrigin(origins = "http://localhost:3000")

public class UserAccessController {

    //localhost:8080/user/register
    @Lazy
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/authenticate")
    public ResponseEntity<String> createAuthenticationToken(@RequestBody UserReq userReq) throws Exception{
        String token = null, refreshToken = null;
        System.out.println("username and pass"+userReq.toString());
        try {
            authenticate(userReq.getUsername(),userReq.getPassword());
            final UserDetails userByUsername =
                    userService.loadUserByUsername(userReq.getUsername());
            token = jwtUtil.generateToken(userByUsername);
//            refreshToken = jwtUtil.generateRefreshToken(userByUsername);
        }catch (Exception e){
            return ResponseEntity.status(403).body(e.getMessage()+" please try again");
        }
//        final org.springframework.security.core.userdetails.UserDetails userByUsername = userService.loadUserByUsername(userDetails.getUserName());
        return ResponseEntity.ok().body(token);
    }
    private void authenticate(String  principal, String  userDetails) throws Exception{
        try {
             Authentication authentication = authenticationManager
                     .authenticate(new UsernamePasswordAuthenticationToken(principal,userDetails));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (DisabledException e){
            throw new Exception("USER DISABLED ",e);
        }catch (BadCredentialsException e){
            throw new Exception("INVALID CREDENTIALS ",e);
        }catch (Exception e){
            throw new Exception("EXCEPTION ",e);
        }
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody EmployeePrimeDetails userDetails){
        return userService.updateUser(userDetails);
    }
    @GetMapping("/profile")
    public ResponseEntity<EmployeePrimeDetails> getCurrentUserDetails(){
        return userService.findCurrentUser();
    }
}
