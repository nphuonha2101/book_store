package com.ecommerce.book_store.http.controller.auth;

import com.ecommerce.book_store.service.abstraction.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "pages/auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "pages/auth/register";
    }

    @PostMapping("/register")
    public String registerPost() {



        return "redirect:/login";
    }

    // logout
}
