package com.example.storeserver.repositories;

import com.example.storeserver.entity.Brand;
import com.example.storeserver.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
}
