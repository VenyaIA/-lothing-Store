package com.example.storeserver.repositories;

import com.example.storeserver.entity.ImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageProductRepository extends JpaRepository<ImageProduct, Long> {

    List<ImageProduct> findAllByProductId(Long productId);

    Optional<ImageProduct> findByIdAndProductId(Long imageProductId, Long productId);

    void deleteByIdAndProductId(Long imageProductId, Long productId);

}
