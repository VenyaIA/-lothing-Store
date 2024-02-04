package com.example.storeserver.repositories;

import com.example.storeserver.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByOrderByCreatedDateDesc();

    List<Product> findAllByBrandOrderByCreatedDateDesc(Brand brand);

    List<Product> findAllByCategoryOrderByCreatedDateDesc(Category category);

}
