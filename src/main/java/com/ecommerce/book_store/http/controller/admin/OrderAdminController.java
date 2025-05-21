package com.ecommerce.book_store.http.controller.admin;

import com.ecommerce.book_store.core.constant.OrderStatus;
import com.ecommerce.book_store.http.dto.response.implement.OrderResponseDto;
import com.ecommerce.book_store.service.abstraction.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/orders")
public class OrderAdminController {
    private final OrderService orderService;

    public OrderAdminController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String index(Pageable pageable, Model model) {
        Page<OrderResponseDto> page = orderService.findAll(pageable);
        model.addAttribute("page", page);
        model.addAttribute("CONTENT_TITLE", "Quản lý đơn hàng");
        model.addAttribute("LAYOUT_TITLE", "Admin BookStore");
        return "pages/admin/orders/index";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        OrderResponseDto order = orderService.findById(id);
        model.addAttribute("order", order);
        model.addAttribute("availableStatuses", orderService.getAvailableStatuses(id));
        model.addAttribute("CONTENT_TITLE", "Chi tiết đơn hàng");
        model.addAttribute("LAYOUT_TITLE", "Admin BookStore");
        return "pages/admin/orders/detail";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam OrderStatus newStatus) {
        try {
            orderService.updateOrderStatus(id, newStatus);
        } catch (IllegalStateException e) {
            return "redirect:/admin/orders/" + id + "?error=" + e.getMessage();
        }
        return "redirect:/admin/orders/" + id;
    }
}
