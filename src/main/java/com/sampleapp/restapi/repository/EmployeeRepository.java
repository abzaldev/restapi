package com.sampleapp.restapi.repository;

import com.sampleapp.restapi.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Page<Employee> findByNameContaining(String name, Pageable pageable);

}
