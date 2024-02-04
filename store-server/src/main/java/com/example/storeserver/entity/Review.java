package com.example.storeserver.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Long userId;

    @Column(columnDefinition = "text", nullable = false)
    private String message;

//    @Column(nullable = false)
//    private Double rating;

    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    @Column(updatable = false)
    private LocalDateTime createdDate;

    @PrePersist // задаёт значение атрибута перед тем как мы сделаем новую запись в БД
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }
}
