package com.ecommerce.book_store.http.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardAdminController {
    @GetMapping(value = {"/admin/dashboard", "/admin"})
    public String index() {
        return "pages/admin/dashboard/dashboard";
    }
}
