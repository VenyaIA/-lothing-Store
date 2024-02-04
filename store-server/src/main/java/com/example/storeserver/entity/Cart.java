package com.example.storeserver.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Customer customer;

    @Column(nullable = false)
    private Double totalPrice;

    @ManyToMany
    private List<Product> products = new ArrayList<>();
}
