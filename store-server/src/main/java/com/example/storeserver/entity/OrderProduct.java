package com.example.storeserver.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;

    @ManyToMany
    private List<Product> products;

    @OneToOne
    private Payment payment;

    @Column(updatable = false)
    private LocalDateTime createdDate;

    @PrePersist // задаёт значение атрибута перед тем как мы сделаем новую запись в БД
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }
}
