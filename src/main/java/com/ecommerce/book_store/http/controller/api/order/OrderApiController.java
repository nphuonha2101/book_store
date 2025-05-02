package com.ecommerce.book_store.http.controller.api.order;

import com.ecommerce.book_store.http.ApiResponse;
import com.ecommerce.book_store.http.dto.request.implement.OrderRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.OrderResponseDto;
import com.ecommerce.book_store.service.abstraction.OrderItemService;
import com.ecommerce.book_store.service.abstraction.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/api/v1/auth/orders")
@RestController
public class OrderApiController {
    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }
    @PostMapping("/order")
    public ResponseEntity<ApiResponse<Object>> order(@RequestBody OrderRequestDto orderRequestDto) {
        try {
            OrderResponseDto orderResponseDto = orderService.order(orderRequestDto);

            return orderResponseDto != null ? ApiResponse.success(orderResponseDto, "Đặt hàng thành công") : ApiResponse.error("Đặt hàng thất bại", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.error("Có lỗi xảy ra", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<OrderResponseDto>>> getOrderHistory(
            @RequestParam Long userId) {
        try {
            List<OrderResponseDto> orderHistory = orderService.getOrderHistory(userId);
            return ApiResponse.success(orderHistory, "Lấy danh sách đơn hàng thành công");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.error("Có lỗi xảy ra khi lấy danh sách đơn hàng", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Hủy đơn hàng
    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<ApiResponse<Object>> cancelOrder(
            @PathVariable Long orderId,
            @RequestParam Long userId) {
        try {
            orderService.cancelOrder(orderId, userId);
            return ApiResponse.success(null, "Hủy đơn hàng thành công");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.error("Có lỗi xảy ra khi hủy đơn hàng", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
