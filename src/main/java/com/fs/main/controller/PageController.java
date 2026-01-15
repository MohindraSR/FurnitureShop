package com.fs.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/customerRegistrationPre")
    public String registerPage() {
        return "CustomerRegistration";
    }

    @GetMapping("/furniture/home")
    public String homePage() {
        return "CustomerHomePage";
    }
}

