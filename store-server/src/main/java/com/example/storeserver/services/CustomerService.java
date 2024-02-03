package com.example.storeserver.services;

import com.example.storeserver.dto.CustomerDTO;
import com.example.storeserver.entity.Customer;
import com.example.storeserver.entity.enums.EnumRole;
import com.example.storeserver.exceptions.CustomerExistException;
import com.example.storeserver.exceptions.CustomerNotFoundException;
import com.example.storeserver.payload.request.SignupRequest;
import com.example.storeserver.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

/**
 * api/customer/ – GET Currently logged in customer
 * api/customer/:customerId  – GET customer data
 * api/customer/update – POST update customer
 */
@Service
public class CustomerService {
    public static final Logger LOG = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CartService cartService;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, BCryptPasswordEncoder passwordEncoder, CartService cartService) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartService = cartService;
    }

    public Customer createCustomer(SignupRequest customerIn) {
        Customer customer = new Customer();
        customer.setEmail(customerIn.getEmail());
        customer.setFirstname(customerIn.getFirstname());
        customer.setLastname(customerIn.getLastname());
        customer.setUsername(customerIn.getUsername());
        customer.setPhoneNumber(customerIn.getPhoneNumber());
        customer.setPassword(passwordEncoder.encode(customerIn.getPassword()));
        customer.getRoles().add(EnumRole.ROLE_USER);

        try {
            LOG.info("Saving customer {}", customer.getEmail());
            Customer createCustomer = customerRepository.save(customer);
            cartService.createCart(createCustomer);
            return createCustomer;
        } catch (Exception e) {
            LOG.error("Error during registration. {}", e.getMessage());
            throw new CustomerExistException("The customer " + customer.getUsername() + " already exist. Please check credentials");
        }
    }

    public Customer getCurrentCustomer(Principal principal) {
        return getCustomerByPrincipal(principal);
    }

    public Customer updateCustomer(CustomerDTO customerDTO, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        customer.setFirstname(customerDTO.getFirstname());
        customer.setLastname(customerDTO.getLastname());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());

        return customerRepository.save(customer);
    }

    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    }

    private Customer getCustomerByPrincipal(Principal principal) {
        String username = principal.getName();
        return customerRepository.findCustomerByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username: " + username));
    }

}
