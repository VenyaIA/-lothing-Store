package com.example.storeserver.repositories;

import com.example.storeserver.entity.Customer;
import com.example.storeserver.entity.ImageProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageProfileRepository extends JpaRepository<ImageProfile, Long> {
    Optional<ImageProfile> findByCustomerId(Long customerId);
}
