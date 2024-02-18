package com.sample.token.controller;

import com.sample.token.entities.UserDetails;
import com.sample.token.security.JwtUtil;
import com.sample.token.services.UserService;
import org.hibernate.type.descriptor.java.ObjectJavaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("user")
public class UserAccessController {

    //localhost:8080/user/register
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/authenticate")
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody UserDetails userDetails) throws Exception{
        try {
            authenticate(userDetails.getUserName(),userDetails.getPassWord());
        }catch (Exception e){
            return ResponseEntity.status(403).body("Invalid credentials, please check details and try again");
        }
        final org.springframework.security.core.userdetails.UserDetails userByUsername = userService.loadUserByUsername(userDetails.getUserName());
        final String token = jwtUtil.generateToken(userByUsername);
        final String refreshToken = jwtUtil.generateRefreshToken(userByUsername);
        return ResponseEntity.ok().body(token);
    }
    private void authenticate(String username, String password) throws Exception{
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        }catch (DisabledException e){
            throw new Exception("USER DISABLED ",e);
        }catch (BadCredentialsException e){
            throw new Exception("INVALID CREDENTIALS ",e);
        }catch (Exception e){
            throw new Exception("EXCEPTION ",e);
        }
    }
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody UserDetails userDetails){
        userDetails.setPassWord(bCryptPasswordEncoder.encode(userDetails.getPassWord()));
        userService.updateUser(userDetails);
        return ResponseEntity.ok().body(userDetails.getUserName()+" details save successfully");
    }
    @GetMapping("/profile")
    public ResponseEntity<Object> getCurrentUserDetails(){
        return ResponseEntity.ok().body(userService.findCurrentUser());
    }
}
