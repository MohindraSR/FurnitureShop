package com.fs.main.service;


import com.fs.main.entity.Customer;
import com.fs.main.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setUserName("test_user");
        customer.setPassword("raw-password");
        customer.setEmail("test@gmail.com");
        customer.setName("Test User");
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }


    //createCustomer()
    @Test
    void shouldCreateCustomerWithEncodedPassword() {

        Mockito.when(passwordEncoder.encode("raw-password"))
                .thenReturn("encoded-password");

        Mockito.when(customerRepository.save(Mockito.any(Customer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Customer savedCustomer = customerService.createCustomer(customer);

        assertNotNull(savedCustomer);
        assertEquals("encoded-password", savedCustomer.getPassword());

        Mockito.verify(passwordEncoder, Mockito.times(1))
                .encode("raw-password");

        Mockito.verify(customerRepository, Mockito.times(1))
                .save(customer);
    }


    // loadUserByUsername()
    @Test
    void shouldLoadUserByUsernameSuccessfully() {

        customer.setPassword("encoded-password");

        Mockito.when(customerRepository.findByUserName("test_user"))
                .thenReturn(Optional.of(customer));

        UserDetails userDetails =
                customerService.loadUserByUsername("test_user");

        assertNotNull(userDetails);
        assertEquals("test_user", userDetails.getUsername());
        assertEquals("encoded-password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }


    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        Mockito.when(customerRepository.findByUserName("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                customerService.loadUserByUsername("unknown"));
    }


    @Test
    void shouldReturnCurrentLoggedInCustomerProfile() {

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "test_user",
                        "password"
                )
        );

        Mockito.when(customerRepository.findByUserName("test_user"))
                .thenReturn(Optional.of(customer));

        Customer currentCustomer =
                customerService.getCurrentCustomerProfile();

        assertNotNull(currentCustomer);
        assertEquals("test_user", currentCustomer.getUserName());

        Mockito.verify(customerRepository, Mockito.times(1))
                .findByUserName("test_user");
    }

    @Test
    void shouldThrowExceptionWhenCurrentCustomerNotFound() {

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "missing_user",
                        "password"
                )
        );

        Mockito.when(customerRepository.findByUserName("missing_user"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                customerService.getCurrentCustomerProfile());

        assertEquals("Customer Not Found", ex.getMessage());
    }
}
