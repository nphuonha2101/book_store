package com.ecommerce.book_store.http.controller.admin;

import com.ecommerce.book_store.core.constant.OrderStatus;
import com.ecommerce.book_store.core.security.JwtUtils;
import com.ecommerce.book_store.http.ApiResponse;
import com.ecommerce.book_store.http.dto.response.implement.OrderResponseDto;
import com.ecommerce.book_store.http.dto.response.implement.UserResponseDto;
import com.ecommerce.book_store.service.abstraction.OrderService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardAdminController {
    private final OrderService orderService;
    public DashboardAdminController(OrderService orderService) {
        this.orderService = orderService;
    }
    @GetMapping({"/admin/dashboard", "/admin"})
    public String dashboard(Model model) {
        try {
            List<OrderResponseDto> recentOrders = orderService.findAll(PageRequest.of(0, 5)).getContent();
            model.addAttribute("recentOrders", recentOrders);
            model.addAttribute("CONTENT_TITLE", "Dashboard");
            model.addAttribute("LAYOUT_TITLE", "Admin BookStore");
            return "pages/admin/dashboard/dashboard";
        } catch (Exception e) {
            return "redirect:/admin/login";
        }
    }

    @GetMapping(value = {"/admin/ajax/chart/order"})
    @ResponseBody
    public ResponseEntity<ApiResponse<Map<Integer, Double>>> getByMonth(
            @RequestParam(value = "month", defaultValue = "0") int month,
            @RequestParam(value = "status", defaultValue = "DELIVERED") String status,
            Model model
    ) {
        try {
            Map<Integer, Double> revenueByMonth = new HashMap<>();
            boolean allStatuses = "ALL".equalsIgnoreCase(status);
            OrderStatus[] statuses = allStatuses ? OrderStatus.values() : new OrderStatus[]{OrderStatus.valueOf(status.toUpperCase())};

            if (month == 0) {
                for (int m = 1; m <= 12; m++) {
                    double total = 0;
                    for (OrderStatus orderStatus : statuses) {
                        Map<String, Double> stats = orderService.getOrderStats(orderStatus, m);
                        total += stats.getOrDefault("totalAmount", 0.0);
                    }
                    revenueByMonth.put(m, total);
                }
            } else {
                double total = 0;
                for (OrderStatus orderStatus : statuses) {
                    Map<String, Double> stats = orderService.getOrderStats(orderStatus, month);
                    total += stats.getOrDefault("totalAmount", 0.0);
                }
                revenueByMonth.put(month, total);
            }
            return ApiResponse.success(revenueByMonth, "Thống kê doanh thu theo tháng");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/admin/ajax/chart/order-status")
    @ResponseBody
    public ResponseEntity<ApiResponse<Map<String, Double>>> getRevenueByStatus(
            @RequestParam(value = "month", defaultValue = "0") int month
    ) {
        try {
            Map<String, Double> revenueByStatus = new HashMap<>();
            for (OrderStatus status : OrderStatus.values()) {
                if (status == OrderStatus.ALL) continue;
                Map<String, Double> stats = orderService.getOrderStats(status, month);
                revenueByStatus.put(status.name(), stats.getOrDefault("totalAmount", 0.0));
            }
            return ApiResponse.success(revenueByStatus, "Thống kê doanh thu theo trạng thái");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
