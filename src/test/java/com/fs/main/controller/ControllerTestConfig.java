package com.fs.main.controller;

import com.fs.main.config.jwt.JwtUtil;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class ControllerTestConfig {

    @Bean
    JwtUtil jwtUtil() {
        return Mockito.mock(JwtUtil.class);
    }

   /* @Bean
    org.springframework.validation.Validator validator(){
        return new org.springframework.validation.beanvalidation.LocalValidatorFactoryBean();
    }*/
}

