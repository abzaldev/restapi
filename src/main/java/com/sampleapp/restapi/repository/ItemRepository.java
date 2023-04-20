package com.sampleapp.restapi.repository;

import com.sampleapp.restapi.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
