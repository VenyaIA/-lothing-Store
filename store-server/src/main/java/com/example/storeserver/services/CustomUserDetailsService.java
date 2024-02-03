package com.example.storeserver.services;

import com.example.storeserver.entity.Customer;
import com.example.storeserver.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private final CustomerRepository customerRepository;

    public CustomUserDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findCustomerByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username: " + username));

        return build(customer);
    }

    public Customer loadCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public static Customer build(Customer customer) {
        List<GrantedAuthority> authorities = customer.getRoles().stream()
                .map(enumRole -> new SimpleGrantedAuthority(enumRole.name()))
                .collect(Collectors.toList());
        return new Customer(
                customer.getId(),
                customer.getUsername(),
                customer.getEmail(),
                customer.getPassword(),
                authorities
        );
    }
}
