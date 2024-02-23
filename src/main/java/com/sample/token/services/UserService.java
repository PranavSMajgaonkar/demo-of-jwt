package com.sample.token.services;

import com.sample.token.repository.UserDetailsRepository;
import com.sample.token.security.SecurityPrincipal;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.sample.token.entities.UserDetails user = userDetailsRepository.findByUserName(username);
        if (user != null){
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(user.getRole()));
            UserDetails principal = new User(user.getUserName(),user.getPassWord(),authorities);
            return principal;
        }
        return null;
    }

    public com.sample.token.entities.UserDetails findByUsername(String username){
        return userDetailsRepository.findByUserName(username);
    }

    public List<com.sample.token.entities.UserDetails> retrieveAllUserList(){
        return userDetailsRepository.findAll();
    }

    public com.sample.token.entities.UserDetails updateUser(com.sample.token.entities.UserDetails userDetails){
        return userDetailsRepository.save(userDetails);
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
