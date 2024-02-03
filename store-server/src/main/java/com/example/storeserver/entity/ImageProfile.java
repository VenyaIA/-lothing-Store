package com.example.storeserver.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ImageProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private Customer customer;
    private Long customerId;

    @Lob
    @Column(columnDefinition = "BIGINT")
    private byte[] image;

}
