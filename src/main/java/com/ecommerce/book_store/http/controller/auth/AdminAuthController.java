package com.ecommerce.book_store.http.controller.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class AdminAuthController {

    @GetMapping("/admin/login")
    public String login(
            Model model
    ) {
        model.addAttribute("CONTENT_TITLE", "Đăng nhập");
        model.addAttribute("LAYOUT_TITLE", "Admin Book Store");

        return "pages/admin/auth/login";
    }
}
