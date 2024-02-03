package com.example.storeserver.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String price;

    private Integer likes;

    @Column
    @ElementCollection(targetClass = String.class)
    private Set<String> likesUsers = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    private Brand brand;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
    private List<Size> sizes = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
    private List<Color> colors = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "product", orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
    private List<Cart> carts = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
    private List<Promotion> promotions = new ArrayList<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "product", orphanRemoval = true)
    private List<ImageProduct> imageProducts = new ArrayList<>();

    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @PrePersist // задаёт значение атрибута перед тем как мы сделаем новую запись в БД
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

    @Transient
    private Collection<? extends GrantedAuthority> authorities;
}
