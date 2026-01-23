package com.fs.main.controller;

import com.fs.main.config.jwt.JwtUtil;
import com.fs.main.dto.CustomerDto;
import com.fs.main.entity.Customer;
import com.fs.main.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AuthenticationManager authenticationManager;
    @MockitoBean
    private JwtUtil jwtUtil;
    @MockitoBean
    private CustomerService customerService;

    private Customer customer;
    private CustomerDto customerDto;
    @BeforeEach
    void setUp(){
        customer = new Customer();
        customer.setUserName("test_login_user");
        customer.setPassword("test@123");
        customer.setName("Test Login");
        customer.setEmail("test@gmail.com");
        customer.setAddress("Test Address");

        customerDto = new CustomerDto();
        customerDto.setUserName("test_login_user");
        customerDto.setPassword("test@123");
    }

    @Test
    void shouldRegisterSuccessfullyAndSetJwtCookie() throws Exception {

        Mockito.when(customerService.createCustomer(Mockito.any(Customer.class)))
                .thenReturn(customer);

        Mockito.when(jwtUtil.generateToken(customer.getUserName()))
                .thenReturn("register-jwt-token");

        mockMvc.perform(post("/api/auth/register")
                        .param("userName", customer.getUserName())
                        .param("password", customer.getPassword())
                        .param("name", customer.getName())
                        .param("email", customer.getEmail())
                        .param("address", customer.getAddress()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/api/user/allProducts"))
                .andExpect(header().string(
                        HttpHeaders.SET_COOKIE,
                        containsString("JWT=register-jwt-token")
                ));
    }


    @Test
    void shouldLoginSuccessfullyAndSetJwtCookie() throws Exception {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(customer.getUserName(), customer.getPassword());

        Mockito.when(authenticationManager.authenticate(Mockito.any()))
                .thenReturn(authentication);

        Mockito.when(jwtUtil.generateToken(customerDto.getUserName()))
                .thenReturn("mock-jwt-token");

        mockMvc.perform(post("/api/auth/login")
                        .param("userName", customerDto.getUserName())
                        .param("password", customerDto.getPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/api/user/allProducts"))
                .andExpect(header().string(
                        HttpHeaders.SET_COOKIE,
                        containsString("JWT=mock-jwt-token")
                ));
    }

}