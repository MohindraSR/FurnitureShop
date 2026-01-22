package com.fs.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    /**
     * Purpose: Display login customer page
     *
     * @return
     */
    @GetMapping("/login")
    public String loginPage() {
        return "Login"; // Login.jsp
    }

    @GetMapping("/register")
    public String registerPage() {
        return "CustomerRegistration";
    }

}

