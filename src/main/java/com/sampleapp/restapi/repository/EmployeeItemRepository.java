package com.sampleapp.restapi.repository;

import com.sampleapp.restapi.model.EmployeeItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeItemRepository extends JpaRepository<EmployeeItem, Long> {
}
