package com.sampleapp.restapi.repository;

import com.sampleapp.restapi.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @NonNull
    @Override
    @EntityGraph(attributePaths = {"employeeItems.item"})
    Optional<Employee> findById(@NonNull Long id);

    @EntityGraph(attributePaths = {"employeeItems.item"})
    Page<Employee> findByNameContaining(String name, Pageable pageable);

}
