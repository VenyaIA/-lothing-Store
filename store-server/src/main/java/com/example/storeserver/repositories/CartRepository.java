package com.example.storeserver.repositories;

import com.example.storeserver.entity.Cart;
import com.example.storeserver.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

}
