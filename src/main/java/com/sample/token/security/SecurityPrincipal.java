package com.sample.token.security;

import com.sample.token.entities.EmployeePrimeDetails;
import com.sample.token.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SecurityPrincipal {
    private Authentication principal = SecurityContextHolder.getContext().getAuthentication();
    private static UserService userService;
    @Autowired
    public SecurityPrincipal(UserService userService) {
        SecurityPrincipal.userService = userService;
    }
    public static SecurityPrincipal getInstance(){
        return new SecurityPrincipal(userService);
    }
    public EmployeePrimeDetails getLoggedInPrincipal(){
        if (principal != null){
            UserDetails loggedInPrincipal = (UserDetails) principal.getPrincipal();
            return userService.findByUsername(loggedInPrincipal.getUsername());
        }
        return null;
    }
    public Collection<?> getLoggedInPrincipalAuthorities(){
        return ((UserDetails) principal.getPrincipal()).getAuthorities();
    }
}
