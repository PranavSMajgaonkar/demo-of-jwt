package com.sample.token.services;

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
import java.util.Optional;

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
        com.sample.token.entities.UserDetails user = userDetailsRepository
                .findByUserName(username);
        if (user != null){
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(user.getRole().toUpperCase()));
            return new User(user.getUserName(),user.getPassWord(),authorities);
        }
        return null;
    }

    public com.sample.token.entities.UserDetails findByUsername(String username){
        return userDetailsRepository.findByUserName(username);
    }

    public List<UserDetailsWraper> retrieveAllUserList(){
        List<com.sample.token.entities.UserDetails> user = userDetailsRepository.findAll();
        List<UserDetailsWraper> list = new ArrayList<>();
        for (com.sample.token.entities.UserDetails u : user){
            list.add(new UserDetailsWraper(u.getId(),u.getFirstName(),u.getUserName(),u.getRole()));
        }
        return list;
    }

    public ResponseEntity<String> updateUser(com.sample.token.entities.UserDetails userDetails){
        if (userDetails != null && isPresent(userDetails)) {
            userDetails.setPassWord(passwordEncoder.encode(userDetails.getPassWord()));
            userDetailsRepository.save(userDetails);
            return ResponseEntity.ok().body(userDetails.getUserName()+" details save successfully!!");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already present!!");
    }
    private boolean isPresent (com.sample.token.entities.UserDetails userDetails){
        com.sample.token.entities.UserDetails user = findByUsername(userDetails.getUserName());
        return user == null;
    }

    public ResponseEntity<com.sample.token.entities.UserDetails> findCurrentUser(){
        try {
            return  new ResponseEntity<>(userDetailsRepository.findByUserName(SecurityPrincipal
                    .getInstance().getLoggedInPrincipal().getUserName()), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
       return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
