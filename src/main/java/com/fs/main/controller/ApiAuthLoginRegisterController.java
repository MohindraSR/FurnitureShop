package com.fs.main.controller;

import com.fs.main.dto.CustomerDto;
import com.fs.main.config.jwt.JwtUtil;
import com.fs.main.service.CustomerService;
import com.fs.main.entity.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/")
public class ApiAuthLoginRegisterController {

    CustomerService customerService;

    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    public ApiAuthLoginRegisterController(AuthenticationManager authenticationManager,
                                          JwtUtil jwtUtil,
                                          CustomerService customerService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.customerService = customerService;
        // DEBUG (temporary)
        System.out.println("AuthenticationManager injected: " + authenticationManager);
    }


    /**
     * Purpose: To save or register customer into database
     * @param customer
     * @return
     */
    @PostMapping("/customerRegistration")
    public Map<String, Object> customerRegistrationPost(@RequestBody Customer customer) {
        customerService.createCustomer(customer);
        String token = jwtUtil.generateToken(customer.getUserName());
        return Collections.singletonMap("jwt-token",token);
    }


    /**
     * Purpose: For login, check credentials
     * if validate then display customer home page
     * else display warning
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> checkLoginCredential(
            @RequestBody CustomerDto customerDto) {

        try {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(
                            customerDto.getUserName(),
                            customerDto.getPassword()
                    );

            authenticationManager.authenticate(authInputToken);

            String token = jwtUtil.generateToken(customerDto.getUserName());

            return ResponseEntity.ok(
                    Collections.singletonMap("jwt-token", token)
            );

        } catch (AuthenticationException ex) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap(
                            "error", "Invalid username or password"
                    ));
        }
    }

}
