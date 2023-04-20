package com.sampleapp.restapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item")
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private double price;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private Set<EmployeeItem> employeeItems = new HashSet<>();

    public Item(String name) {
        this.name = name;
    }

    public Item(String name, double price) {
        this.name = name;
        this.price = price;
    }
}
