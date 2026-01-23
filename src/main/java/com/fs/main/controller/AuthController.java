package com.fs.main.controller;

import com.fs.main.dto.CustomerDto;
import com.fs.main.config.jwt.JwtUtil;
import com.fs.main.service.CustomerService;
import com.fs.main.entity.Customer;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.parser.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {
    @Autowired
    CustomerService customerService;

    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          CustomerService customerService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.customerService = customerService;

        System.out.println("AuthenticationManager injected: " + authenticationManager);
    }


    /**
     * Purpose: To save or register customer into database
     * @param customer
     * @return
     */
//    @PostMapping("/register")
    /*public Map<String, Object> customerRegistrationPost(@RequestBody Customer customer) {
        customerService.createCustomer(customer);
        String token = jwtUtil.generateToken(customer.getUserName());
        return Collections.singletonMap("jwt-token",token);
    }*/

    @PostMapping("/register")
    public ModelAndView register(Customer customer, ModelAndView modelAndView, HttpServletResponse response){

        Customer createdCustomer = customerService.createCustomer(customer);
        String token = jwtUtil.generateToken(customer.getUserName());

        ResponseCookie cookie = ResponseCookie.from("JWT", token)
                .httpOnly(true)
                .secure(false) // true in production (HTTPS)
                .path("/")
                .maxAge(Duration.ofHours(1))
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        modelAndView.setViewName("redirect:/api/user/allProducts");

        return modelAndView;
    }


    /**
     * Purpose: For login, check credentials
     * if validate then display customer home page
     * else display warning
     */
//    @PostMapping("/login")
    /*public ResponseEntity<Map<String, Object>> checkLoginCredential(
            @RequestBody  CustomerDto customerDto) {

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
    }*/

    @PostMapping("/login")
    public ModelAndView login(
             CustomerDto customerDto,
            HttpServletResponse response) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    customerDto.getUserName(),
                                    customerDto.getPassword()
                            )
                    );

            String token = jwtUtil.generateToken(authentication.getName());

            ResponseCookie jwtCookie = ResponseCookie.from("JWT", token)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(Duration.ofMinutes(30))
                    .sameSite("Strict")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

            return new ModelAndView("redirect:/api/user/allProducts");

        } catch (AuthenticationException ex) {
            ModelAndView mv = new ModelAndView("Login");
            mv.addObject("error", "Invalid username or password");
            return mv;
        }
    }
}
