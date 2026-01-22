package com.fs.main.service;

import com.fs.main.repository.CustomerRepository;
import com.fs.main.entity.Customer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomerService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Purpose: Create new customer
     * @param customer
     */
    public Customer createCustomer(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return customerRepository.save(customer);
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Customer customer =
                customerRepository.findByUserName(username)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "User not found: " + username
                                )
                        );

        // Convert DB user → Spring Security user
        return new org.springframework.security.core.userdetails.User(
                customer.getUserName(),
                customer.getPassword(), // encoded password
                Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_USER")
                )
        );
    }

    /**
     * Purpose: Get Logged in Customer Details
     * @return
     */
    public Customer getCurrentCustomerProfile(){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        return customerRepository.findByUserName(userName).orElseThrow(() -> new
                RuntimeException("Customer Not Found"));
    }
}