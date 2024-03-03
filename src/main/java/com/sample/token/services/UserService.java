package com.sample.token.services;

import com.sample.token.entities.EmployeePrimeDetails;
import com.sample.token.model.UserDetailsWraper;
import com.sample.token.repository.UserDetailsRepository;
import com.sample.token.security.JwtUtil;
import com.sample.token.security.SecurityPrincipal;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*In this class you have to extends UserDetailsService which is a inbuilt spring class need to implement method if loadUserByUserName()
* in this method call your userDetailsRepository method which is findByUserNameAndPassWord()*/
@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private  UserDetailsRepository userDetailsRepository;
    @Autowired
    JwtUtil jwtUtil;
    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EmployeePrimeDetails employeePrimeDetails = userDetailsRepository
                .findByUserName(username);
        if (employeePrimeDetails != null){
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(employeePrimeDetails.getRole().toUpperCase()));
            return new User(employeePrimeDetails.getUserName(),employeePrimeDetails.getPassWord(),authorities);
        }
        return null;
    }

    public EmployeePrimeDetails findByUsername(String username){
        return userDetailsRepository.findByUserName(username);
    }

    public List<UserDetailsWraper> retrieveAllUserList(){
        List<EmployeePrimeDetails> employeePrimeDetails = userDetailsRepository.findAll();
        List<UserDetailsWraper> list = new ArrayList<>();
        for (EmployeePrimeDetails u : employeePrimeDetails){
            list.add(new UserDetailsWraper(u.getId(),u.getFirstName(),u.getUserName(),u.getRole()));
        }
        return list;
    }

    public ResponseEntity<String> updateUser(EmployeePrimeDetails employeePrimeDetails){
        if (employeePrimeDetails != null && isPresent(employeePrimeDetails)) {
            employeePrimeDetails.setPassWord(passwordEncoder.encode(employeePrimeDetails.getPassWord()));
            userDetailsRepository.save(employeePrimeDetails);
            return ResponseEntity.ok().body(employeePrimeDetails.getUserName()+" details save successfully!!");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already present!!");
    }
    private boolean isPresent (EmployeePrimeDetails employeePrimeDetails){
        EmployeePrimeDetails user = findByUsername(employeePrimeDetails.getUserName());
        return user == null;
    }

    public ResponseEntity<EmployeePrimeDetails> findCurrentUser(){
        try {
            return  new ResponseEntity<>(userDetailsRepository.findByUserName(SecurityPrincipal
                    .getInstance().getLoggedInPrincipal().getUserName()), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
       return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
