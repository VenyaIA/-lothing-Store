package com.example.storeserver.repositories;

import com.example.storeserver.entity.Customer;
import com.example.storeserver.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    List<OrderProduct> findAllByCustomer(Customer customer);


}
