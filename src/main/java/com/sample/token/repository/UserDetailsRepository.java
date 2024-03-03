package com.sample.token.repository;

import com.sample.token.entities.EmployeePrimeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository extends JpaRepository<EmployeePrimeDetails, Integer > {
    EmployeePrimeDetails findByUserName(String username);
}
