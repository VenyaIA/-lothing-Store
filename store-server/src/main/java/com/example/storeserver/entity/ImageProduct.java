package com.example.storeserver.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ImageProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    @Column(nullable = false)
    private String url;

}
