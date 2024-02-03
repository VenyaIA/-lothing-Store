package com.example.storeserver.repositories;

import com.example.storeserver.entity.Product;
import com.example.storeserver.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {
//    List<Size> findAllByProducts(List<Product> products);
}
