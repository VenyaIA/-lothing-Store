package com.example.storeserver.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String paymentType;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String status;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private OrderProduct orderProduct;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @PrePersist // задаёт значение атрибута перед тем как мы сделаем новую запись в БД
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

}
