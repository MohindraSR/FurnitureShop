package com.fs.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class AuthPageController {

    @GetMapping("/home")
    public String homePage() {
        return "CustomerHomePage";
    }
}
