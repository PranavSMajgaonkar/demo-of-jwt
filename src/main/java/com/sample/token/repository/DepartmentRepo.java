package com.sample.token.repository;

import com.sample.token.entities.Department;
import com.sample.token.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface DepartmentRepo extends JpaRepository<Department,Long> {

//    @Query(value = "select * from employee where first_name = :firstname")
//    Employee findByEmployee(@Param("firstname") String firstname);
}
