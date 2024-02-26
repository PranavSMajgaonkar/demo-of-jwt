package com.sample.token.controller;

import com.mysql.cj.x.protobuf.Mysqlx;
import com.sample.token.entities.UserDetails;
import com.sample.token.security.JwtUtil;
import com.sample.token.security.SecurityPrincipal;
import com.sample.token.services.UserService;
import org.hibernate.type.descriptor.java.ObjectJavaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.http.HttpResponse;
import java.util.Collection;

@Controller
@RequestMapping("user")
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
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody UserDetails userDetails) throws Exception{
        String token = null, refreshToken = null;
        try {
            authenticate(userDetails.getUserName(),userDetails.getPassWord());
            final org.springframework.security.core.userdetails.UserDetails userByUsername =
                    userService.loadUserByUsername(userDetails.getUserName());
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
    public ResponseEntity<String> register(@RequestBody UserDetails userDetails){
        return userService.updateUser(userDetails);
    }
    @GetMapping("/profile")
    public ResponseEntity<UserDetails> getCurrentUserDetails(){
        return userService.findCurrentUser();
    }
}
