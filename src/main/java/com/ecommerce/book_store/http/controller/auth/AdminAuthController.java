package com.ecommerce.book_store.http.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminAuthController {

    @GetMapping("/admin/login")
    public String login(Model model) {
        model.addAttribute("CONTENT_TITLE", "Đăng nhập");
        model.addAttribute("LAYOUT_TITLE", "Admin Book Store");

        return "pages/admin/auth/login";
    }

    @PostMapping("/admin/login")
    public String login() {
        return "redirect:/admin/dashboard";
    }
}
