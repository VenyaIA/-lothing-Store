package com.example.storeserver.facade;

import com.example.storeserver.dto.CustomerDTO;
import com.example.storeserver.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerFacade {

    public CustomerDTO customerToCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setFirstname(customer.getFirstname());
        customerDTO.setLastname(customer.getLastname());
        customerDTO.setUsername(customer.getUsername());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.getRoles().addAll(customer.getRoles());

        return customerDTO;
    }

}
