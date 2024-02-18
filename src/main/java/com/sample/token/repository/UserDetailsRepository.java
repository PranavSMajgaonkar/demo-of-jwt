package com.sample.token.repository;

import com.sample.token.entities.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, String > {
    UserDetails findByUserNameAndPassWord(String username, String password);
    UserDetails findByUserName(String username);

}
